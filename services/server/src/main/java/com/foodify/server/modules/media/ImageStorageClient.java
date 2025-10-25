package com.foodify.server.modules.media;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.foodify.server.config.ImageServiceProperties;
import java.io.IOException;
import java.net.URI;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.HttpStatus.BAD_GATEWAY;

@Service
public class ImageStorageClient {
    private static final Logger log = LoggerFactory.getLogger(ImageStorageClient.class);

    private final RestTemplate restTemplate;
    private final ImageServiceProperties properties;

    public ImageStorageClient(ImageServiceProperties properties,
                              @Qualifier("imageServiceRestTemplate") RestTemplate restTemplate) {
        this.properties = Objects.requireNonNull(properties, "properties must not be null");
        this.restTemplate = Objects.requireNonNull(restTemplate, "restTemplate must not be null");
    }

    public String store(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new ResponseStatusException(BAD_GATEWAY, "Image file is empty");
        }

        Resource resource = asResource(file);
        HttpHeaders fileHeaders = new HttpHeaders();
        MediaType mediaType = resolveMediaType(file.getContentType());
        fileHeaders.setContentType(mediaType);
        HttpEntity<Resource> filePart = new HttpEntity<>(resource, fileHeaders);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", filePart);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        URI uploadUri = resolvePath(properties.getEffectiveBaseUrl(), "/images");
        ResponseEntity<ImageUploadResponse> response;
        try {
            response = restTemplate.postForEntity(uploadUri, requestEntity, ImageUploadResponse.class);
        } catch (RestClientException ex) {
            log.warn("Failed to upload image to {}", uploadUri, ex);
            throw new ResponseStatusException(BAD_GATEWAY, "Failed to store image", ex);
        }

        ImageUploadResponse payload = response.getBody();
        if (payload == null) {
            throw new ResponseStatusException(BAD_GATEWAY, "Image service returned an empty response");
        }

        if (StringUtils.hasText(payload.url())) {
            return payload.url();
        }

        if (StringUtils.hasText(payload.id())) {
            return resolvePublicUrl(payload.id());
        }

        throw new ResponseStatusException(BAD_GATEWAY, "Image service response did not include an identifier");
    }

    public String resolvePublicUrl(String identifier) {
        if (!StringUtils.hasText(identifier)) {
            return identifier;
        }
        if (identifier.startsWith("http://") || identifier.startsWith("https://")) {
            return identifier;
        }
        URI publicBase = properties.getEffectivePublicBaseUrl();
        String normalized = identifier.startsWith("/") ? identifier.substring(1) : identifier;
        return resolvePath(publicBase, normalized).toString();
    }

    private Resource asResource(MultipartFile file) throws IOException {
        String filename = file.getOriginalFilename();
        if (!StringUtils.hasText(filename)) {
            filename = file.getName();
        }
        String sanitized = StringUtils.cleanPath(filename);
        byte[] content = file.getBytes();
        return new ByteArrayResource(content) {
            @Override
            public String getFilename() {
                return sanitized;
            }

            @Override
            public long contentLength() {
                return content.length;
            }
        };
    }

    private MediaType resolveMediaType(String contentType) {
        if (!StringUtils.hasText(contentType)) {
            return MediaType.APPLICATION_OCTET_STREAM;
        }
        try {
            return MediaType.parseMediaType(contentType);
        } catch (IllegalArgumentException ex) {
            log.debug("Invalid content type '{}', defaulting to application/octet-stream", contentType, ex);
            return MediaType.APPLICATION_OCTET_STREAM;
        }
    }

    private URI resolvePath(URI base, String path) {
        String baseValue = base.toString();
        if (!baseValue.endsWith("/")) {
            baseValue = baseValue + "/";
        }
        if (path.startsWith("/")) {
            path = path.substring(1);
        }
        return URI.create(baseValue + path);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private record ImageUploadResponse(String id, String url) {}
}
