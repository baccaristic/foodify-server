package com.foodify.server.modules.notifications.application;

import com.foodify.server.modules.notifications.domain.NotificationType;
import com.foodify.server.modules.notifications.domain.UserDevice;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.niamedtech.expo.exposerversdk.ExpoPushNotificationClient;
import com.niamedtech.expo.exposerversdk.request.PushNotification;
import com.niamedtech.expo.exposerversdk.response.TicketResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class PushNotificationService {

    @Autowired
    private UserDeviceService userDeviceService;

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

    public void sendPushNotification(Long userId, String title, String body) throws IOException {
        List<UserDevice> devices = userDeviceService.findByUser(userId);
        CloseableHttpClient httpClient = HttpClients.createDefault();
        ExpoPushNotificationClient client = ExpoPushNotificationClient
                .builder()
                .setHttpClient(httpClient)
                .setAccessToken(System.getenv("EXPO_ACCESS_TOKEN"))
                .build();

        PushNotification pushNotification = new PushNotification();
        pushNotification.setTo(devices.stream().map(UserDevice::getDeviceToken).toList());
        pushNotification.setTitle(title);
        pushNotification.setBody(body);
        List<PushNotification> notifications = new ArrayList<>();
        notifications.add(pushNotification);
        List<TicketResponse.Ticket> response = client.sendPushNotifications(notifications);
        for (TicketResponse.Ticket ticket : response) {
            System.out.println(ticket.getId());
            System.out.println(ticket.getStatus());
        }
    }
}
