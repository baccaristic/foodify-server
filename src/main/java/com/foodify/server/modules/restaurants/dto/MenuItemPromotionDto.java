package com.foodify.server.modules.restaurants.dto;

public record MenuItemPromotionDto(
        Long id,
        String name,
        // Multi-language support
        String nameEn,
        String nameFr,
        String nameAr,
        double price,
        Double promotionPrice,
        String promotionLabel,
        String imageUrl,
        boolean favorite
) {
}
