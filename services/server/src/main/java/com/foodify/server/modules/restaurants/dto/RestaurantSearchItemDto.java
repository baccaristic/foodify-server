package com.foodify.server.modules.restaurants.dto;

import com.foodify.server.modules.restaurants.domain.RestaurantCategory;

import java.util.List;
import java.util.Set;

public record RestaurantSearchItemDto(
        Long id,
        String name,
        String deliveryTimeRange,
        Double rating,
        boolean isTopChoice,
        boolean hasFreeDelivery,
        Double deliveryFee,
        boolean favorite,
        String imageUrl,
        String iconUrl,
        Set<RestaurantCategory> categories,
        List<MenuItemPromotionDto> promotedMenuItems
) {
}
