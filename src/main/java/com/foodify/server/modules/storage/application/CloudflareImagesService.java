package com.foodify.server.modules.storage.application;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.foodify.server.config.CloudflareImagesProperties;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class CloudflareImagesService {

    private final CloudflareImagesProperties properties;
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Uploads an image to Cloudflare Images CDN or local storage based on configuration.
     *
     * @param file the image file to upload
     * @return the public URL of the uploaded image
     * @throws IOException if upload fails
     */
    public String uploadImage(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File cannot be null or empty");
        }

        if (properties.getLocal().isEnabled()) {
            return storeLocally(file);
        }

        validateConfiguration();

        String originalFilename = file.getOriginalFilename();
        String sanitizedFilename = StringUtils.hasText(originalFilename) ? originalFilename : file.getName();

        String boundary = "----WebKitFormBoundary" + Long.toHexString(System.currentTimeMillis());
        String CRLF = "\r\n";

        String uploadUrl = String.format("%s/accounts/%s/images/v1",
                properties.getBaseUrl(),
                properties.getAccountId());

        URL url = new URL(uploadUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.setRequestProperty("Authorization", "Bearer " + properties.getApiToken());
        connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

        try (OutputStream output = connection.getOutputStream();
             PrintWriter writer = new PrintWriter(new OutputStreamWriter(output, StandardCharsets.UTF_8), true)) {

            // Add file field
            writer.append("--").append(boundary).append(CRLF);
            writer.append("Content-Disposition: form-data; name=\"file\"; filename=\"")
                    .append(sanitizedFilename).append("\"").append(CRLF);
            writer.append("Content-Type: ").append(getContentType(file)).append(CRLF);
            writer.append(CRLF).flush();

            // Write binary file data directly to output stream
            file.getInputStream().transferTo(output);
            output.flush();

            // Write closing boundary - must use output stream directly after binary data
            writer.append(CRLF);
            writer.append("--").append(boundary).append("--").append(CRLF);
            writer.flush();
        }

        int responseCode = connection.getResponseCode();

        if (responseCode == HttpURLConnection.HTTP_OK) {
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }

                return parseUploadResponse(response.toString());
            }
        } else {
            String errorMessage = readErrorStream(connection);
            log.error("Cloudflare Images upload failed with status {}: {}", responseCode, errorMessage);
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Failed to upload image to Cloudflare: " + errorMessage
            );
        }
    }

    private String storeLocally(MultipartFile file) throws IOException {
        CloudflareImagesProperties.LocalStorageProperties local = properties.getLocal();
        Path storageDir = Paths.get(local.getDirectory()).toAbsolutePath().normalize();
        Files.createDirectories(storageDir);

        String extension = getFileExtension(file.getOriginalFilename());
        String filename = UUID.randomUUID() + (StringUtils.hasText(extension) ? "." + extension : "");
        Path target = storageDir.resolve(filename);

        try (InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, target, StandardCopyOption.REPLACE_EXISTING);
        }

        String baseUrl = local.getBaseUrl();
        if (baseUrl.endsWith("/")) {
            baseUrl = baseUrl.substring(0, baseUrl.length() - 1);
        }
        return baseUrl + "/" + filename;
    }

    private String parseUploadResponse(String jsonResponse) throws IOException {
        JsonNode root = objectMapper.readTree(jsonResponse);

        if (!root.path("success").asBoolean()) {
            String errorMsg = root.path("errors").toString();
            throw new IOException("Cloudflare Images upload failed: " + errorMsg);
        }

        JsonNode result = root.path("result");
        JsonNode variants = result.path("variants");

        // Get the public variant URL (usually the first one, or 'public' variant)
        if (variants.isArray() && variants.size() > 0) {
            return variants.get(0).asText();
        }

        // Fallback to constructing URL from result
        String imageId = result.path("id").asText();
        if (StringUtils.hasText(imageId)) {
            // Return the variant URL that Cloudflare provides
            // Format: https://imagedelivery.net/{account_hash}/{image_id}/public
            return result.path("variants").get(0).asText();
        }

        throw new IOException("Failed to extract image URL from Cloudflare response");
    }

    private String readErrorStream(HttpURLConnection connection) {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(connection.getErrorStream(), StandardCharsets.UTF_8))) {
            StringBuilder error = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                error.append(line);
            }
            return error.toString();
        } catch (Exception e) {
            return "Unable to read error details";
        }
    }

    private String getContentType(MultipartFile file) {
        String contentType = file.getContentType();
        if (StringUtils.hasText(contentType)) {
            return contentType;
        }

        // Fallback based on file extension
        String filename = file.getOriginalFilename();
        if (filename != null) {
            if (filename.endsWith(".jpg") || filename.endsWith(".jpeg")) {
                return "image/jpeg";
            } else if (filename.endsWith(".png")) {
                return "image/png";
            } else if (filename.endsWith(".gif")) {
                return "image/gif";
            } else if (filename.endsWith(".webp")) {
                return "image/webp";
            }
        }

        return "application/octet-stream";
    }

    private void validateConfiguration() {
        if (properties.getLocal().isEnabled()) {
            return;
        }
        if (!StringUtils.hasText(properties.getAccountId())) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Cloudflare Images account ID is not configured"
            );
        }
        if (!StringUtils.hasText(properties.getApiToken())) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Cloudflare Images API token is not configured"
            );
        }
    }

    private String getFileExtension(String filename) {
        if (!StringUtils.hasText(filename)) {
            return null;
        }
        int idx = filename.lastIndexOf('.') + 1;
        if (idx <= 0 || idx >= filename.length()) {
            return null;
        }
        return filename.substring(idx);
    }

    @Data
    private static class CloudflareUploadResponse {
        private boolean success;
        private CloudflareImageResult result;
        private JsonNode errors;
    }

    @Data
    private static class CloudflareImageResult {
        private String id;
        private String filename;
        private String[] variants;
        private boolean uploaded;

        @JsonProperty("requireSignedURLs")
        private boolean requireSignedURLs;
    }
}