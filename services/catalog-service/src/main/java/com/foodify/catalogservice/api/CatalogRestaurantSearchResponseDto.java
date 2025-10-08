package com.foodify.catalogservice.api;

import java.util.List;

public record CatalogRestaurantSearchResponseDto(
        List<CatalogRestaurantSearchItemDto> items,
        int page,
        int pageSize,
        long totalItems
) {
}
