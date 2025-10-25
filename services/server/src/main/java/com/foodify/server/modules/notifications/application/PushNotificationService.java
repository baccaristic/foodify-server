package com.foodify.server.modules.notifications.application;

import com.foodify.server.modules.notifications.domain.NotificationType;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import org.springframework.stereotype.Service;

@Service
public class PushNotificationService {

    public void sendOrderNotification(String deviceToken, Long orderId, String title, String body, NotificationType type) throws Exception {
        Message message = Message.builder()
                .setToken(deviceToken)
                .setNotification(Notification.builder()
                        .setTitle(title)
                        .setBody(body)
                        .build())
                .putData("orderId", String.valueOf(orderId))
                .putData("notificationType", String.valueOf(type))// optional: for in-app handling
                .build();

        String response = FirebaseMessaging.getInstance().send(message);
        System.out.println("Successfully sent message: " + response);
    }
}
