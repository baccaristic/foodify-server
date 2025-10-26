package com.foodify.authservice.modules.delivery.client;

import com.foodify.authservice.config.properties.AuthServiceProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

import static org.springframework.util.StringUtils.hasText;

@Component
@RequiredArgsConstructor
public class DriverSessionClient {

    private final RestTemplate restTemplate;
    private final AuthServiceProperties properties;

    public String startSession(Long driverId, String deviceId) {
        String serviceId = properties.getServer().getServiceId();
        String secret = properties.getServer().getInternalSecret();
        if (!hasText(serviceId)) {
            throw new IllegalStateException("Server service ID is not configured for auth-service");
        }
        if (!hasText(secret)) {
            throw new IllegalStateException("Internal shared secret is not configured");
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-Internal-Secret", secret);

        Map<String, Object> payload = Map.of(
                "driverId", driverId,
                "deviceId", deviceId
        );

        try {
            Map<String, Object> response = restTemplate.postForObject(
                    "http://" + serviceId + "/internal/driver-sessions",
                    new HttpEntity<>(payload, headers),
                    Map.class
            );
            if (response == null) {
                throw new IllegalStateException("Driver session service returned empty response");
            }
            Object token = response.get("sessionToken");
            if (token == null) {
                throw new IllegalStateException("Driver session service did not return a session token");
            }
            return token.toString();
        } catch (RestClientException ex) {
            throw new IllegalStateException("Failed to create driver session", ex);
        }
    }
}
