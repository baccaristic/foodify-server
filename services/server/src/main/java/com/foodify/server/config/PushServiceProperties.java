package com.foodify.server.config;

import java.net.URI;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.StringUtils;

@ConfigurationProperties(prefix = "push-service")
public class PushServiceProperties {
    private static final URI DEFAULT_BASE_URI = URI.create("http://push-service");

    private URI baseUrl;
    private String serviceId = "push-service";

    public URI getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(URI baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public URI getEffectiveBaseUrl() {
        if (baseUrl != null) {
            return baseUrl;
        }
        if (StringUtils.hasText(serviceId)) {
            return URI.create("http://" + serviceId);
        }
        return DEFAULT_BASE_URI;
    }
}
