package com.foodify.server.modules.storage.application;

import com.foodify.server.config.CloudflareImagesProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;

class CloudflareImagesServiceTest {

    private CloudflareImagesProperties properties;
    private CloudflareImagesService cloudflareImagesService;

    @BeforeEach
    void setUp() {
        properties = new CloudflareImagesProperties();
        properties.setAccountId("test-account-id");
        properties.setApiToken("test-api-token");
        properties.setBaseUrl("https://api.cloudflare.com/client/v4");
        
        cloudflareImagesService = new CloudflareImagesService(properties);
    }

    @Test
    void uploadImage_shouldThrowException_whenFileIsNull() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> cloudflareImagesService.uploadImage(null)
        );
        
        assertEquals("File cannot be null or empty", exception.getMessage());
    }

    @Test
    void uploadImage_shouldThrowException_whenFileIsEmpty() {
        MockMultipartFile emptyFile = new MockMultipartFile(
            "file",
            "test.jpg",
            "image/jpeg",
            new byte[0]
        );
        
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> cloudflareImagesService.uploadImage(emptyFile)
        );
        
        assertEquals("File cannot be null or empty", exception.getMessage());
    }

    @Test
    void uploadImage_shouldThrowException_whenAccountIdIsNotConfigured() {
        properties.setAccountId(null);
        
        MockMultipartFile file = new MockMultipartFile(
            "file",
            "test.jpg",
            "image/jpeg",
            "test image content".getBytes()
        );
        
        ResponseStatusException exception = assertThrows(
            ResponseStatusException.class,
            () -> cloudflareImagesService.uploadImage(file)
        );
        
        assertTrue(exception.getReason().contains("account ID is not configured"));
    }

    @Test
    void uploadImage_shouldThrowException_whenApiTokenIsNotConfigured() {
        properties.setApiToken(null);
        
        MockMultipartFile file = new MockMultipartFile(
            "file",
            "test.jpg",
            "image/jpeg",
            "test image content".getBytes()
        );
        
        ResponseStatusException exception = assertThrows(
            ResponseStatusException.class,
            () -> cloudflareImagesService.uploadImage(file)
        );
        
        assertTrue(exception.getReason().contains("API token is not configured"));
    }

    @Test
    void uploadImage_shouldThrowException_whenApiTokenIsEmpty() {
        properties.setApiToken("");
        
        MockMultipartFile file = new MockMultipartFile(
            "file",
            "test.jpg",
            "image/jpeg",
            "test image content".getBytes()
        );
        
        ResponseStatusException exception = assertThrows(
            ResponseStatusException.class,
            () -> cloudflareImagesService.uploadImage(file)
        );
        
        assertTrue(exception.getReason().contains("API token is not configured"));
    }
}
