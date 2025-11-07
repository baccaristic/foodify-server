package com.foodify.server.modules.notifications.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.Map;

public record TargetedPushNotificationRequest(
        @NotBlank(message = "Title is required")
        @Size(max = 100, message = "Title must not exceed 100 characters")
        String title,

        @NotBlank(message = "Body is required")
        @Size(max = 500, message = "Body must not exceed 500 characters")
        String body,

        Map<String, Object> data // Optional: additional data to include in notification
) {
}
