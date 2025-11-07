package com.foodify.server.modules.notifications.api;

import com.foodify.server.modules.notifications.application.PushNotificationService;
import com.foodify.server.modules.notifications.dto.BulkPushNotificationRequest;
import com.foodify.server.modules.notifications.dto.PushNotificationResponse;
import com.foodify.server.modules.notifications.dto.TargetedPushNotificationRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/notifications")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
@Tag(name = "Admin Push Notifications", description = "Admin APIs for sending push notifications")
@SecurityRequirement(name = "bearerAuth")
public class AdminPushNotificationController {

    private final PushNotificationService pushNotificationService;

    @PostMapping("/push/bulk")
    @Operation(
            summary = "Send bulk push notifications",
            description = "Send push notifications to all users or filter by role (CLIENT, DRIVER, RESTAURANT_ADMIN). " +
                    "This endpoint allows admins to send customizable notifications with optional data payload."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Notifications sent successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Admin role required")
    })
    public ResponseEntity<PushNotificationResponse> sendBulkNotifications(
            @Valid @RequestBody BulkPushNotificationRequest request
    ) {
        PushNotificationResponse response = pushNotificationService.sendBulkPushNotification(
                request.title(),
                request.body(),
                request.targetRole(),
                request.data()
        );
        return ResponseEntity.ok(response);
    }

    @PostMapping("/push/user/{userId}")
    @Operation(
            summary = "Send push notification to specific user",
            description = "Send a push notification to all devices of a specific user by their user ID. " +
                    "Supports custom data payload for rich notifications."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Notification sent successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Admin role required"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<PushNotificationResponse> sendTargetedNotification(
            @Parameter(description = "User ID to send notification to")
            @PathVariable Long userId,
            @Valid @RequestBody TargetedPushNotificationRequest request
    ) {
        PushNotificationResponse response = pushNotificationService.sendTargetedPushNotification(
                userId,
                request.title(),
                request.body(),
                request.data()
        );
        return ResponseEntity.ok(response);
    }
}
