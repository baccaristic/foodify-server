package com.foodify.server.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.HttpMethod;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@ConfigurationProperties(prefix = "guardrails")
public class GuardrailProperties {

    private List<RateLimit> rateLimits = new ArrayList<>();

    public List<RateLimit> getRateLimits() {
        return rateLimits;
    }

    public void setRateLimits(List<RateLimit> rateLimits) {
        this.rateLimits = rateLimits != null ? rateLimits : Collections.emptyList();
    }

    public static class RateLimit {
        private String name;
        private List<String> paths = new ArrayList<>();
        private HttpMethod method = HttpMethod.GET;
        private int capacity = 100;
        private Duration window = Duration.ofSeconds(1);
        private int httpStatus = 429;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<String> getPaths() {
            return paths;
        }

        public void setPaths(List<String> paths) {
            this.paths = paths != null ? paths : Collections.emptyList();
        }

        public HttpMethod getMethod() {
            return method;
        }

        public void setMethod(HttpMethod method) {
            this.method = method != null ? method : HttpMethod.GET;
        }

        public int getCapacity() {
            return capacity;
        }

        public void setCapacity(int capacity) {
            this.capacity = capacity;
        }

        public Duration getWindow() {
            return window;
        }

        public void setWindow(Duration window) {
            this.window = window != null ? window : Duration.ofSeconds(1);
        }

        public int getHttpStatus() {
            return httpStatus;
        }

        public void setHttpStatus(int httpStatus) {
            this.httpStatus = httpStatus;
        }
    }
}
