package com.foodify.pushservice.model;

import com.foodify.pushservice.domain.NotificationType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record SendOrderNotificationRequest(
        @NotBlank String deviceToken,
        @NotNull Long orderId,
        @NotBlank String title,
        @NotBlank String body,
        @NotNull NotificationType notificationType
) {}
