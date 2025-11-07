package com.foodify.server.modules.notifications.dto;

import com.foodify.server.modules.identity.domain.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.Map;

public record BulkPushNotificationRequest(
        @NotBlank(message = "Title is required")
        @Size(max = 100, message = "Title must not exceed 100 characters")
        String title,

        @NotBlank(message = "Body is required")
        @Size(max = 500, message = "Body must not exceed 500 characters")
        String body,

        Role targetRole, // Optional: filter by role (CLIENT, DRIVER, etc). If null, sends to all users

        Map<String, Object> data // Optional: additional data to include in notification
) {
}
