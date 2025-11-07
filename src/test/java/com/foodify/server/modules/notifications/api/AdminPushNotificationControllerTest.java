package com.foodify.server.modules.notifications.api;

import com.foodify.server.modules.identity.domain.Role;
import com.foodify.server.modules.notifications.application.PushNotificationService;
import com.foodify.server.modules.notifications.dto.BulkPushNotificationRequest;
import com.foodify.server.modules.notifications.dto.PushNotificationResponse;
import com.foodify.server.modules.notifications.dto.TargetedPushNotificationRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AdminPushNotificationControllerTest {

    @Mock
    private PushNotificationService pushNotificationService;

    private AdminPushNotificationController controller;

    @BeforeEach
    void setUp() {
        controller = new AdminPushNotificationController(pushNotificationService);
    }

    @Test
    void shouldSendBulkNotificationsSuccessfully() {
        // Arrange
        Map<String, Object> data = new HashMap<>();
        data.put("type", "promotional");
        data.put("category", "food");

        BulkPushNotificationRequest request = new BulkPushNotificationRequest(
                "Test Title",
                "Test Body",
                Role.CLIENT,
                data
        );

        PushNotificationResponse expectedResponse = new PushNotificationResponse(
                100,
                150,
                145,
                5,
                "Sent push notifications to 150 devices (145 success, 5 failure)"
        );

        when(pushNotificationService.sendBulkPushNotification(
                anyString(),
                anyString(),
                any(Role.class),
                any(Map.class)
        )).thenReturn(expectedResponse);

        // Act
        ResponseEntity<PushNotificationResponse> response = controller.sendBulkNotifications(request);

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(100, response.getBody().totalUsers());
        assertEquals(150, response.getBody().totalDevices());
        assertEquals(145, response.getBody().successCount());
        assertEquals(5, response.getBody().failureCount());

        verify(pushNotificationService).sendBulkPushNotification(
                eq("Test Title"),
                eq("Test Body"),
                eq(Role.CLIENT),
                eq(data)
        );
    }

    @Test
    void shouldSendBulkNotificationsToAllUsersWhenRoleIsNull() {
        // Arrange
        BulkPushNotificationRequest request = new BulkPushNotificationRequest(
                "Test Title",
                "Test Body",
                null, // No role filter - send to all users
                null
        );

        PushNotificationResponse expectedResponse = new PushNotificationResponse(
                500,
                750,
                740,
                10,
                "Sent push notifications to 750 devices (740 success, 10 failure)"
        );

        when(pushNotificationService.sendBulkPushNotification(
                anyString(),
                anyString(),
                eq(null),
                eq(null)
        )).thenReturn(expectedResponse);

        // Act
        ResponseEntity<PushNotificationResponse> response = controller.sendBulkNotifications(request);

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(500, response.getBody().totalUsers());
        assertEquals(750, response.getBody().totalDevices());
    }

    @Test
    void shouldSendTargetedNotificationSuccessfully() {
        // Arrange
        Long userId = 123L;
        Map<String, Object> data = new HashMap<>();
        data.put("orderId", 456);

        TargetedPushNotificationRequest request = new TargetedPushNotificationRequest(
                "Order Update",
                "Your order is on the way!",
                data
        );

        PushNotificationResponse expectedResponse = new PushNotificationResponse(
                1,
                2,
                2,
                0,
                "Sent push notification to 2 devices (2 success, 0 failure)"
        );

        when(pushNotificationService.sendTargetedPushNotification(
                anyLong(),
                anyString(),
                anyString(),
                any(Map.class)
        )).thenReturn(expectedResponse);

        // Act
        ResponseEntity<PushNotificationResponse> response = controller.sendTargetedNotification(userId, request);

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().totalUsers());
        assertEquals(2, response.getBody().totalDevices());
        assertEquals(2, response.getBody().successCount());
        assertEquals(0, response.getBody().failureCount());

        verify(pushNotificationService).sendTargetedPushNotification(
                eq(123L),
                eq("Order Update"),
                eq("Your order is on the way!"),
                eq(data)
        );
    }

    @Test
    void shouldHandleTargetedNotificationWithNoDevices() {
        // Arrange
        Long userId = 999L;
        TargetedPushNotificationRequest request = new TargetedPushNotificationRequest(
                "Test Title",
                "Test Body",
                null
        );

        PushNotificationResponse expectedResponse = new PushNotificationResponse(
                1,
                0,
                0,
                0,
                "No devices found for user"
        );

        when(pushNotificationService.sendTargetedPushNotification(
                anyLong(),
                anyString(),
                anyString(),
                eq(null)
        )).thenReturn(expectedResponse);

        // Act
        ResponseEntity<PushNotificationResponse> response = controller.sendTargetedNotification(userId, request);

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(0, response.getBody().totalDevices());
        assertEquals("No devices found for user", response.getBody().message());
    }
}
