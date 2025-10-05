package com.foodify.server.modules.restaurants.dto;

public record MenuItemPromotionDto(
        Long id,
        String name,
        double price,
        Double promotionPrice,
        String promotionLabel
) {
}
