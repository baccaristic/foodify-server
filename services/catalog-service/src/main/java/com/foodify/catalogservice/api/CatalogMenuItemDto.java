package com.foodify.catalogservice.api;

import java.util.List;

public record CatalogMenuItemDto(
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
        List<CatalogMenuItemExtraDto> extras
) {
}
