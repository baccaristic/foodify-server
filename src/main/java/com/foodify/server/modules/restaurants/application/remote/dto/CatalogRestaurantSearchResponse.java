package com.foodify.server.modules.restaurants.application.remote.dto;

import java.util.List;

public record CatalogRestaurantSearchResponse(
        List<CatalogRestaurantSearchItemResponse> items,
        int page,
        int pageSize,
        long totalItems
) {
}
