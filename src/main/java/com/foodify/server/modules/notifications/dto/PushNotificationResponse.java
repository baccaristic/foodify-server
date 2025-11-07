package com.foodify.server.modules.notifications.dto;

public record PushNotificationResponse(
        int totalUsers,
        int totalDevices,
        int successCount,
        int failureCount,
        String message
) {
}
