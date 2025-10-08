package com.foodify.catalogservice.api;

public record CatalogMenuItemPromotionDto(
        Long id,
        String name,
        double price,
        Double promotionPrice,
        String promotionLabel,
        String imageUrl
) {
}
