package com.foodify.server.modules.delivery.dto;

import java.time.LocalDateTime;

public record DeliveryRatingResponse(
        Long orderId,
        Long deliveryId,
        Long driverId,
        Long clientId,
        String clientName,
        Integer timing,
        Integer foodCondition,
        Integer professionalism,
        Integer overall,
        String comments,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
