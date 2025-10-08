package com.foodify.server.modules.restaurants.dto;

import java.util.List;

public record RestaurantSearchItemDto(
        Long id,
        String name,
        String deliveryTimeRange,
        Double rating,
        boolean isTopChoice,
        boolean hasFreeDelivery,
        String imageUrl,
        List<MenuItemPromotionDto> promotedMenuItems
) {
}
