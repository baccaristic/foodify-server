package com.foodify.server.modules.restaurants.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@ConfigurationProperties(prefix = "catalog.service")
public class CatalogServiceProperties {

    private ServiceMode mode = ServiceMode.MONOLITH;
    private final Remote remote = new Remote();

    public ServiceMode getMode() {
        return mode;
    }

    public void setMode(ServiceMode mode) {
        this.mode = mode;
    }

    public Remote getRemote() {
        return remote;
    }

    public enum ServiceMode {
        MONOLITH,
        REMOTE
    }

    public static class Remote {
        private String baseUrl = "http://localhost:8086";
        private Duration connectTimeout = Duration.ofSeconds(2);
        private Duration readTimeout = Duration.ofSeconds(5);

        public String getBaseUrl() {
            return baseUrl;
        }

        public void setBaseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
        }

        public Duration getConnectTimeout() {
            return connectTimeout;
        }

        public void setConnectTimeout(Duration connectTimeout) {
            this.connectTimeout = connectTimeout;
        }

        public Duration getReadTimeout() {
            return readTimeout;
        }

        public void setReadTimeout(Duration readTimeout) {
            this.readTimeout = readTimeout;
        }
    }
}
