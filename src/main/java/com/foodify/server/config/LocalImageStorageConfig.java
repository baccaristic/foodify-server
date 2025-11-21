package com.foodify.server.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
@ConditionalOnProperty(prefix = "cloudflare.images.local", name = "enabled", havingValue = "true")
public class LocalImageStorageConfig implements WebMvcConfigurer {

    private final CloudflareImagesProperties properties;

    public LocalImageStorageConfig(CloudflareImagesProperties properties) {
        this.properties = properties;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        CloudflareImagesProperties.LocalStorageProperties local = properties.getLocal();
        Path storagePath = Paths.get(local.getDirectory()).toAbsolutePath().normalize();
        String location = storagePath.toUri().toString();
        registry.addResourceHandler("/local-images/**")
                .addResourceLocations(location.endsWith("/") ? location : location + "/");
    }
}