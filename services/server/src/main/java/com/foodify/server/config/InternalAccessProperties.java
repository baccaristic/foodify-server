package com.foodify.server.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "internal")
@Data
public class InternalAccessProperties {
    private String sharedSecret;
}
