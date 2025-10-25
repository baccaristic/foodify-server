package com.foodify.imageservice;

import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "images")
public class StorageProperties {
    /**
     * Root directory where image payloads will be stored. Defaults to {@code uploads} relative to the working directory.
     */
    @NotBlank
    private String location = "uploads";

    /**
     * Public base URL used to build downloadable links returned to upstream services.
     */
    private String publicBaseUrl;

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPublicBaseUrl() {
        return publicBaseUrl;
    }

    public void setPublicBaseUrl(String publicBaseUrl) {
        this.publicBaseUrl = publicBaseUrl;
    }
}
