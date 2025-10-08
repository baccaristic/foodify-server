package com.foodify.server.modules.restaurants.application.remote.dto;

import java.util.List;

public record CatalogMenuItemResponse(
        Long id,
        Long restaurantId,
        String name,
        String description,
        String category,
        boolean popular,
        double price,
        String promotionLabel,
        Double promotionPrice,
        Boolean promotionActive,
        List<String> imageUrls,
        List<CatalogMenuItemExtraResponse> extras
) {
}
