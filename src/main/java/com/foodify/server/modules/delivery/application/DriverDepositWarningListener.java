package com.foodify.server.modules.delivery.application;

import com.foodify.server.modules.identity.domain.Driver;
import com.foodify.server.modules.identity.repository.DriverRepository;
import com.foodify.server.modules.notifications.application.PushNotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class DriverDepositWarningListener {
    private final DriverRepository driverRepository;
    private final PushNotificationService pushNotificationService;
    private final SimpMessagingTemplate messagingTemplate;

    @EventListener
    public void handleDriverDepositWarning(DriverDepositWarningEvent event) {
        if (event == null || event.driverId() == null) {
            return;
        }

        Long driverId = event.driverId();
        Driver driver = driverRepository.findById(driverId).orElse(null);
        if (driver == null) {
            log.warn("Driver {} not found for deposit warning", driverId);
            return;
        }

        String title = "Deposit Required";
        String body = String.format(
            "You have reached %sdt cash on hand. Please deposit the cash within %d hours to continue working.",
            DriverFinancialService.DEPOSIT_THRESHOLD,
            DriverFinancialService.DEPOSIT_DEADLINE_HOURS
        );

        // Send push notification
        try {
            pushNotificationService.sendPushNotification(driverId, title, body);
            log.info("Sent deposit warning push notification to driver {}", driverId);
        } catch (IOException e) {
            log.error("Failed to send deposit warning push notification to driver {}", driverId, e);
        }

        // Send WebSocket notification
        try {
            Map<String, Object> message = new HashMap<>();
            message.put("type", "DEPOSIT_WARNING");
            message.put("title", title);
            message.put("message", body);
            message.put("cashOnHand", driver.getCashOnHand());
            message.put("depositThreshold", DriverFinancialService.DEPOSIT_THRESHOLD);
            message.put("deadlineHours", DriverFinancialService.DEPOSIT_DEADLINE_HOURS);
            
            messagingTemplate.convertAndSendToUser(
                driverId.toString(),
                "/queue/warnings",
                message
            );
            log.info("Sent deposit warning WebSocket message to driver {}", driverId);
        } catch (Exception e) {
            log.error("Failed to send deposit warning WebSocket message to driver {}", driverId, e);
        }
    }
}
