package com.foodify.server.modules.notifications.api;

import com.foodify.server.modules.notifications.application.NotificationPreferenceService;
import com.foodify.server.modules.notifications.dto.NotificationPreferenceResponse;
import com.foodify.server.modules.notifications.dto.NotificationPreferencesUpdateRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications/preferences")
@RequiredArgsConstructor
public class NotificationPreferenceController {

    private final NotificationPreferenceService notificationPreferenceService;

    @GetMapping
    public ResponseEntity<List<NotificationPreferenceResponse>> getPreferences(Authentication authentication) {
        Long userId = extractUserId(authentication);
        return ResponseEntity.ok(notificationPreferenceService.getPreferencesForUser(userId));
    }

    @PutMapping
    public ResponseEntity<List<NotificationPreferenceResponse>> updatePreferences(
            Authentication authentication,
            @Valid @RequestBody NotificationPreferencesUpdateRequest request
    ) {
        Long userId = extractUserId(authentication);
        return ResponseEntity.ok(notificationPreferenceService.updatePreferences(userId, request));
    }

    @PostMapping("/enable-all")
    public ResponseEntity<List<NotificationPreferenceResponse>> enableAll(Authentication authentication) {
        Long userId = extractUserId(authentication);
        return ResponseEntity.ok(notificationPreferenceService.enableAll(userId));
    }

    private Long extractUserId(Authentication authentication) {
        if (authentication == null || authentication.getPrincipal() == null) {
            throw new IllegalStateException("Authenticated user not found");
        }
        return Long.parseLong(authentication.getPrincipal().toString());
    }
}
