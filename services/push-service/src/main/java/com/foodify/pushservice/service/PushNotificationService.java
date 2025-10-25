package com.foodify.pushservice.service;

import com.foodify.pushservice.model.SendOrderNotificationRequest;
import com.google.api.core.ApiFuture;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class PushNotificationService {

    private static final Logger log = LoggerFactory.getLogger(PushNotificationService.class);
    private static final Executor DIRECT_EXECUTOR = Runnable::run;

    private final FirebaseMessaging firebaseMessaging;

    public PushNotificationService(FirebaseMessaging firebaseMessaging) {
        this.firebaseMessaging = firebaseMessaging;
    }

    @Async
    public void sendOrderNotification(SendOrderNotificationRequest request) {
        Message message = Message.builder()
                .setToken(request.deviceToken())
                .setNotification(Notification.builder()
                        .setTitle(request.title())
                        .setBody(request.body())
                        .build())
                .putData("orderId", String.valueOf(request.orderId()))
                .putData("notificationType", request.notificationType().name())
                .build();

        ApiFuture<String> future = firebaseMessaging.sendAsync(message);
        future.addListener(() -> handleResult(future, request), DIRECT_EXECUTOR);
    }

    private void handleResult(ApiFuture<String> future, SendOrderNotificationRequest request) {
        try {
            String messageId = future.get();
            log.debug("Sent push notification {} for order {}", messageId, request.orderId());
        } catch (ExecutionException ex) {
            log.warn("Failed to send push notification for order {}", request.orderId(), ex.getCause());
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            log.warn("Push notification thread interrupted for order {}", request.orderId(), ex);
        }
    }
}
