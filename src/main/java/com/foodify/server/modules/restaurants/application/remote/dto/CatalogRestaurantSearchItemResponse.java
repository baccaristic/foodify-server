package com.foodify.server.modules.restaurants.application.remote.dto;

import java.util.List;

public record CatalogRestaurantSearchItemResponse(
        Long id,
        String name,
        String deliveryTimeRange,
        Double rating,
        boolean topChoice,
        boolean freeDelivery,
        String imageUrl,
        List<CatalogMenuItemPromotionResponse> promotedMenuItems
) {
}
