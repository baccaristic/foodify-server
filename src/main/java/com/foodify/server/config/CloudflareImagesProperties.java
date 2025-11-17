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
}
