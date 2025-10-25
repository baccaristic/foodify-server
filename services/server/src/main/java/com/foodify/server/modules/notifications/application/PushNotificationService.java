package com.foodify.server.modules.notifications.application;

import com.foodify.server.modules.notifications.domain.NotificationType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;

@Service
public class PushNotificationService {

    private final PushNotificationClient pushNotificationClient;

    public PushNotificationService(PushNotificationClient pushNotificationClient) {
        this.pushNotificationClient = pushNotificationClient;
    }

    public void sendOrderNotification(String deviceToken, Long orderId, String title, String body, NotificationType type) throws Exception {
        try {
            pushNotificationClient.sendOrderNotification(deviceToken, orderId, title, body, type);
        } catch (RestClientException ex) {
            throw new Exception("Failed to dispatch push notification", ex);
        }
    }
}
