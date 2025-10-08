package com.foodify.catalogservice.api;

import java.util.List;

public record CatalogRestaurantSearchItemDto(
        Long id,
        String name,
        String deliveryTimeRange,
        Double rating,
        boolean topChoice,
        boolean freeDelivery,
        String imageUrl,
        List<CatalogMenuItemPromotionDto> promotedMenuItems
) {
}
