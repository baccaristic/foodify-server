package com.foodify.server.modules.restaurants.dto;

public record DeliveryFeeResponse(
        boolean available,
        Double distanceKm,
        Double deliveryFee
) {
}
