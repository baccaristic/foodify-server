package com.foodify.server.modules.admin.dto;

public record DeliveryFeeResponse(
        boolean available,
        Double distanceKm,
        Double deliveryFee
) {
}
