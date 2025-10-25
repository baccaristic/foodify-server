package com.foodify.imageservice;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
public class ImageController {
    private static final Logger log = LoggerFactory.getLogger(ImageController.class);

    private final ImageStorageService storageService;
    private final StorageProperties properties;

    public ImageController(ImageStorageService storageService, StorageProperties properties) {
        this.storageService = storageService;
        this.properties = properties;
    }

    @PostMapping(path = "/images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ImageUploadResponse> upload(@RequestParam("file") MultipartFile file) {
        try {
            ImageStorageService.StoredImage stored = storageService.store(file);
            return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ImageUploadResponse(stored.id(), buildPublicUrl(stored.id())));
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage(), ex);
        } catch (IOException ex) {
            log.error("Failed to store image", ex);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to store image", ex);
        }
    }

    @GetMapping("/images/{id}")
    public ResponseEntity<Resource> serve(@PathVariable String id) {
        Path path = storageService.resolve(id);
        if (!Files.exists(path) || !Files.isReadable(path)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Image not found");
        }

        MediaType mediaType = probeMediaType(path);
        Resource resource = new FileSystemResource(path);
        return ResponseEntity.ok()
            .contentType(mediaType)
            .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=" + path.getFileName())
            .body(resource);
    }

    @DeleteMapping("/images/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        try {
            boolean deleted = storageService.delete(id);
            if (!deleted) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.noContent().build();
        } catch (IOException ex) {
            log.error("Failed to delete image {}", id, ex);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to delete image", ex);
        }
    }

    private String buildPublicUrl(String identifier) {
        if (!StringUtils.hasText(identifier)) {
            return identifier;
        }
        String configuredBase = properties.getPublicBaseUrl();
        if (StringUtils.hasText(configuredBase)) {
            String normalizedBase = configuredBase.endsWith("/") ? configuredBase : configuredBase + "/";
            return normalizedBase + identifier;
        }
        return ServletUriComponentsBuilder.fromCurrentContextPath()
            .path("/images/")
            .path(identifier)
            .build()
            .toUriString();
    }

    private MediaType probeMediaType(Path path) {
        try {
            String detected = Files.probeContentType(path);
            if (StringUtils.hasText(detected)) {
                return MediaType.parseMediaType(detected);
            }
        } catch (IOException | IllegalArgumentException ex) {
            log.debug("Unable to determine content type for {}", path, ex);
        }
        return MediaType.APPLICATION_OCTET_STREAM;
    }

    public record ImageUploadResponse(String id, String url) {}
}
