package com.foodify.server.modules.notifications.dto;

import com.foodify.server.modules.notifications.domain.NotificationType;

import java.time.Instant;

public record NotificationPreferenceResponse(
        NotificationType type,
        boolean enabled,
        Instant updatedAt
) {}
