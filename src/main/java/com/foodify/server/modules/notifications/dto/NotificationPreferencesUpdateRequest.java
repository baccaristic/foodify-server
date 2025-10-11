package com.foodify.server.modules.notifications.dto;

import com.foodify.server.modules.notifications.domain.NotificationType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record NotificationPreferencesUpdateRequest(
        @NotNull(message = "preferences is required")
        List<@Valid PreferenceUpdate> preferences
) {
    public record PreferenceUpdate(
            @NotNull(message = "type is required") NotificationType type,
            @NotNull(message = "enabled is required") Boolean enabled
    ) {
    }
}
