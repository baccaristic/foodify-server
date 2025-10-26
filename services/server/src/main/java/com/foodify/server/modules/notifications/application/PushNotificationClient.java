package com.foodify.server.modules.notifications.application;

import com.foodify.server.config.PushServiceProperties;
import com.foodify.server.modules.notifications.domain.NotificationType;
import java.net.URI;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Component
public class PushNotificationClient {

    private final PushServiceProperties properties;
    private final RestTemplate restTemplate;

    public PushNotificationClient(PushServiceProperties properties,
                                  @Qualifier("pushServiceRestTemplate") RestTemplate restTemplate) {
        this.properties = Objects.requireNonNull(properties, "properties must not be null");
        this.restTemplate = Objects.requireNonNull(restTemplate, "restTemplate must not be null");
    }

    public void sendOrderNotification(String deviceToken, Long orderId, String title, String body, NotificationType type)
            throws RestClientException {
        URI endpoint = resolvePath(properties.getEffectiveBaseUrl(), "/notifications/orders");
        SendOrderNotificationRequest payload = new SendOrderNotificationRequest(deviceToken, orderId, title, body, type);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<SendOrderNotificationRequest> request = new HttpEntity<>(payload, headers);
        ResponseEntity<SendNotificationResponse> response = restTemplate.postForEntity(endpoint, request, SendNotificationResponse.class);
        if (response.getStatusCode().is2xxSuccessful()) {
            return;
        }
        throw new RestClientException("Push service returned status " + response.getStatusCode());
    }

    private URI resolvePath(URI base, String path) {
        String baseValue = base.toString();
        if (!baseValue.endsWith("/")) {
            baseValue = baseValue + "/";
        }
        if (StringUtils.hasText(path) && path.startsWith("/")) {
            path = path.substring(1);
        }
        return URI.create(baseValue + path);
    }

    private record SendOrderNotificationRequest(String deviceToken,
                                                Long orderId,
                                                String title,
                                                String body,
                                                NotificationType notificationType) {
    }

    private record SendNotificationResponse(String status) {
    }
}
