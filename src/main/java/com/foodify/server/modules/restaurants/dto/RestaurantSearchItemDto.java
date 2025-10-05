package com.foodify.server.modules.restaurants.dto;

public record RestaurantSearchItemDto(
        Long id,
        String name,
        String deliveryTimeRange,
        Double rating,
        boolean isTopChoice,
        boolean hasFreeDelivery,
        String promotionLabel,
        String imageUrl
) {
}
