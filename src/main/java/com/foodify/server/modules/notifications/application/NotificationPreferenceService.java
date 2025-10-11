package com.foodify.server.modules.notifications.application;

import com.foodify.server.modules.identity.domain.User;
import com.foodify.server.modules.identity.repository.UserRepository;
import com.foodify.server.modules.notifications.domain.NotificationPreference;
import com.foodify.server.modules.notifications.domain.NotificationType;
import com.foodify.server.modules.notifications.dto.NotificationPreferenceResponse;
import com.foodify.server.modules.notifications.dto.NotificationPreferencesUpdateRequest;
import com.foodify.server.modules.notifications.repository.NotificationPreferenceRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationPreferenceService {

    private static final boolean DEFAULT_ENABLED = true;

    private final NotificationPreferenceRepository preferenceRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<NotificationPreferenceResponse> getPreferencesForUser(Long userId) {
        Map<NotificationType, NotificationPreference> existing = loadPreferencesByType(userId);
        return Arrays.stream(NotificationType.values())
                .map(type -> toResponse(existing.get(type), type))
                .toList();
    }

    @Transactional
    public List<NotificationPreferenceResponse> updatePreferences(Long userId, NotificationPreferencesUpdateRequest request) {
        if (request == null || request.preferences() == null || request.preferences().isEmpty()) {
            return getPreferencesForUser(userId);
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Map<NotificationType, NotificationPreference> existing = new EnumMap<>(loadPreferencesByType(userId));

        for (NotificationPreferencesUpdateRequest.PreferenceUpdate update : request.preferences()) {
            if (update == null || update.type() == null || update.enabled() == null) {
                continue;
            }

            NotificationPreference preference = existing.get(update.type());
            if (preference == null) {
                preference = new NotificationPreference();
                preference.setUser(user);
                preference.setNotificationType(update.type());
            }
            preference.setEnabled(update.enabled());
            preferenceRepository.save(preference);
            existing.put(update.type(), preference);
        }

        return Arrays.stream(NotificationType.values())
                .map(type -> toResponse(existing.get(type), type))
                .toList();
    }

    @Transactional(readOnly = true)
    public boolean isEnabled(Long userId, NotificationType type) {
        return preferenceRepository.findByUserIdAndNotificationType(userId, type)
                .map(pref -> Boolean.TRUE.equals(pref.getEnabled()))
                .orElse(DEFAULT_ENABLED);
    }

    private Map<NotificationType, NotificationPreference> loadPreferencesByType(Long userId) {
        return preferenceRepository.findByUserId(userId).stream()
                .collect(Collectors.toMap(
                        NotificationPreference::getNotificationType,
                        Function.identity(),
                        (existing, duplicate) -> duplicate,
                        () -> new EnumMap<>(NotificationType.class)
                ));
    }

    private NotificationPreferenceResponse toResponse(NotificationPreference preference, NotificationType type) {
        boolean enabled = preference != null ? Boolean.TRUE.equals(preference.getEnabled()) : DEFAULT_ENABLED;
        Instant updatedAt = preference != null ? preference.getUpdatedAt() : null;
        return new NotificationPreferenceResponse(type, enabled, updatedAt);
    }
}
