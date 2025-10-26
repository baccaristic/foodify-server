package com.foodify.server.config;

import java.net.URI;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.StringUtils;

@ConfigurationProperties(prefix = "image-service")
public class ImageServiceProperties {
    private static final URI DEFAULT_BASE_URI = URI.create("http://image-service");

    private URI baseUrl;
    private URI publicBaseUrl;
    private String serviceId = "image-service";

    public URI getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(URI baseUrl) {
        this.baseUrl = baseUrl;
    }

    public URI getPublicBaseUrl() {
        return publicBaseUrl;
    }

    public void setPublicBaseUrl(URI publicBaseUrl) {
        this.publicBaseUrl = publicBaseUrl;
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

    public URI getEffectivePublicBaseUrl() {
        if (publicBaseUrl != null) {
            return publicBaseUrl;
        }
        URI base = getEffectiveBaseUrl();
        String value = base.toString();
        if (!value.endsWith("/")) {
            value = value + "/";
        }
        return URI.create(value + "images");
    }
}
