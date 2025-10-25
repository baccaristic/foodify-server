package com.foodify.pushservice.controller;

import com.foodify.pushservice.model.SendNotificationResponse;
import com.foodify.pushservice.model.SendOrderNotificationRequest;
import com.foodify.pushservice.service.PushNotificationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/notifications")
public class PushNotificationController {

    private final PushNotificationService pushNotificationService;

    public PushNotificationController(PushNotificationService pushNotificationService) {
        this.pushNotificationService = pushNotificationService;
    }

    @PostMapping("/orders")
    public ResponseEntity<SendNotificationResponse> sendOrderNotification(
            @Valid @RequestBody SendOrderNotificationRequest request) {
        pushNotificationService.sendOrderNotification(request);
        return ResponseEntity.accepted().body(new SendNotificationResponse("accepted"));
    }
}
