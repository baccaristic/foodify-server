package com.foodify.server.controller;

import com.foodify.server.dto.OrderRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {
    private final KafkaTemplate<String, OrderRequest> kafkaTemplate;

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('ROLE_CLIENT')")
    public ResponseEntity<Void> createOrder(@RequestBody OrderRequest orderRequest, Authentication auth) {
        Long userId = Long.parseLong((String) auth.getPrincipal());
        if (orderRequest.getUserId() == null || !orderRequest.getUserId().equals(userId)) {
            orderRequest.setUserId(userId);
        }
        kafkaTemplate.send("orders", orderRequest);
        return ResponseEntity.accepted().build();
    }
}
