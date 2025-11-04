package com.foodify.server.modules.admin.dto;

public record MenuItemPromotionDto(
        Long id,
        String name,
        double price,
        Double promotionPrice,
        String promotionLabel,
        String imageUrl,
        boolean favorite
) {
}
