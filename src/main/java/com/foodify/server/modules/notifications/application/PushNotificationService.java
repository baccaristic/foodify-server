package com.foodify.server.modules.notifications.application;

import com.foodify.server.modules.identity.domain.Role;
import com.foodify.server.modules.identity.domain.User;
import com.foodify.server.modules.identity.repository.UserRepository;
import com.foodify.server.modules.notifications.domain.NotificationType;
import com.foodify.server.modules.notifications.domain.UserDevice;
import com.foodify.server.modules.notifications.dto.PushNotificationResponse;
import com.foodify.server.modules.notifications.repository.UserDeviceRepository;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.niamedtech.expo.exposerversdk.ExpoPushNotificationClient;
import com.niamedtech.expo.exposerversdk.request.PushNotification;
import com.niamedtech.expo.exposerversdk.response.TicketResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PushNotificationService {

    private static final Logger logger = LoggerFactory.getLogger(PushNotificationService.class);

    @Autowired
    private UserDeviceService userDeviceService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserDeviceRepository userDeviceRepository;

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

    /**
     * Send bulk push notifications to all users or filtered by role
     */
    public PushNotificationResponse sendBulkPushNotification(
            String title,
            String body,
            Role targetRole,
            Map<String, Object> data
    ) {
        logger.info("Starting bulk push notification. Title: {}, TargetRole: {}", title, targetRole);

        // Get all users or filter by role
        List<User> targetUsers;
        if (targetRole != null) {
            targetUsers = userRepository.findAll().stream()
                    .filter(user -> user.getRole() == targetRole)
                    .collect(Collectors.toList());
        } else {
            targetUsers = userRepository.findAll();
        }

        logger.info("Found {} target users", targetUsers.size());

        // Get all devices for target users
        List<UserDevice> devices = new ArrayList<>();
        for (User user : targetUsers) {
            devices.addAll(userDeviceRepository.findByUserId(user.getId()));
        }

        logger.info("Found {} devices for target users", devices.size());

        if (devices.isEmpty()) {
            return new PushNotificationResponse(
                    targetUsers.size(),
                    0,
                    0,
                    0,
                    "No devices found for target users"
            );
        }

        // Send notifications via Expo
        int successCount = 0;
        int failureCount = 0;

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            ExpoPushNotificationClient client = ExpoPushNotificationClient
                    .builder()
                    .setHttpClient(httpClient)
                    .setAccessToken(System.getenv("EXPO_ACCESS_TOKEN"))
                    .build();

            // Batch devices (Expo recommends batches of 100)
            List<List<String>> batches = batchDeviceTokens(
                    devices.stream().map(UserDevice::getDeviceToken).collect(Collectors.toList()),
                    100
            );

            for (List<String> batch : batches) {
                PushNotification pushNotification = new PushNotification();
                pushNotification.setTo(batch);
                pushNotification.setTitle(title);
                pushNotification.setBody(body);

                if (data != null && !data.isEmpty()) {
                    pushNotification.setData(data);
                }

                List<PushNotification> notifications = new ArrayList<>();
                notifications.add(pushNotification);

                try {
                    List<TicketResponse.Ticket> response = client.sendPushNotifications(notifications);
                    for (TicketResponse.Ticket ticket : response) {
                        if ("ok".equals(ticket.getStatus())) {
                            successCount++;
                        } else {
                            failureCount++;
                            logger.warn("Failed to send notification: {}", ticket.getMessage());
                        }
                    }
                } catch (Exception e) {
                    logger.error("Error sending notification batch", e);
                    failureCount += batch.size();
                }
            }
        } catch (IOException e) {
            logger.error("Error creating HTTP client", e);
            failureCount = devices.size();
        }

        String message = String.format("Sent push notifications to %d devices (%d success, %d failure)",
                devices.size(), successCount, failureCount);
        logger.info(message);

        return new PushNotificationResponse(
                targetUsers.size(),
                devices.size(),
                successCount,
                failureCount,
                message
        );
    }

    /**
     * Send targeted push notification to a specific user (by userId)
     */
    public PushNotificationResponse sendTargetedPushNotification(
            Long userId,
            String title,
            String body,
            Map<String, Object> data
    ) {
        logger.info("Sending targeted push notification to userId: {}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        List<UserDevice> devices = userDeviceRepository.findByUserId(userId);

        if (devices.isEmpty()) {
            return new PushNotificationResponse(
                    1,
                    0,
                    0,
                    0,
                    "No devices found for user"
            );
        }

        int successCount = 0;
        int failureCount = 0;

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            ExpoPushNotificationClient client = ExpoPushNotificationClient
                    .builder()
                    .setHttpClient(httpClient)
                    .setAccessToken(System.getenv("EXPO_ACCESS_TOKEN"))
                    .build();

            PushNotification pushNotification = new PushNotification();
            pushNotification.setTo(devices.stream().map(UserDevice::getDeviceToken).collect(Collectors.toList()));
            pushNotification.setTitle(title);
            pushNotification.setBody(body);

            if (data != null && !data.isEmpty()) {
                pushNotification.setData(data);
            }

            List<PushNotification> notifications = new ArrayList<>();
            notifications.add(pushNotification);

            try {
                List<TicketResponse.Ticket> response = client.sendPushNotifications(notifications);
                for (TicketResponse.Ticket ticket : response) {
                    if ("ok".equals(ticket.getStatus())) {
                        successCount++;
                    } else {
                        failureCount++;
                        logger.warn("Failed to send notification: {}", ticket.getMessage());
                    }
                }
            } catch (Exception e) {
                logger.error("Error sending notification", e);
                failureCount = devices.size();
            }
        } catch (IOException e) {
            logger.error("Error creating HTTP client", e);
            failureCount = devices.size();
        }

        String message = String.format("Sent push notification to %d devices (%d success, %d failure)",
                devices.size(), successCount, failureCount);
        logger.info(message);

        return new PushNotificationResponse(
                1,
                devices.size(),
                successCount,
                failureCount,
                message
        );
    }

    /**
     * Helper method to batch device tokens
     */
    private List<List<String>> batchDeviceTokens(List<String> tokens, int batchSize) {
        List<List<String>> batches = new ArrayList<>();
        for (int i = 0; i < tokens.size(); i += batchSize) {
            batches.add(tokens.subList(i, Math.min(i + batchSize, tokens.size())));
        }
        return batches;
    }
}
