package com.foodify.server.modules.restaurants.application.remote.dto;

import java.util.List;

public record CatalogRestaurantResponse(
        Long id,
        String name,
        String address,
        String phone,
        String type,
        Double rating,
        String openingHours,
        String closingHours,
        String description,
        String imageUrl,
        Boolean topChoice,
        Boolean freeDelivery,
        Boolean topEat,
        Double deliveryFee,
        String deliveryTimeRange,
        List<CatalogMenuItemSummary> menuItems
) {
    public record CatalogMenuItemSummary(Long id) {}
}
