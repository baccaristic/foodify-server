package com.foodify.authservice.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "auth-service")
@Data
public class AuthServiceProperties {
    private Server server = new Server();

    @Data
    public static class Server {
        private String serviceId = "server";
        private String internalSecret;
    }
}
