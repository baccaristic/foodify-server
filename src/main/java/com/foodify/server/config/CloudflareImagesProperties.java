package com.foodify.server.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "cloudflare.images")
public class CloudflareImagesProperties {
    private String accountId;
    private String apiToken;
    private String baseUrl = "https://api.cloudflare.com/client/v4";
    private LocalStorageProperties local = new LocalStorageProperties();

    @Data
    public static class LocalStorageProperties {
        private boolean enabled = false;
        private String directory = "./local-images";
        private String baseUrl = "http://localhost:8081/local-images";
    }
}
