package com.foodify.server.modules.restaurants.application.remote.dto;

public record CatalogMenuItemPromotionResponse(
        Long id,
        String name,
        double price,
        Double promotionPrice,
        String promotionLabel,
        String imageUrl
) {
}
