package com.foodify.server.modules.restaurants.application.remote.dto;

public record CatalogMenuItemExtraResponse(
        Long id,
        Long optionGroupId,
        Long menuItemId,
        String name,
        double price,
        boolean isDefault
) {
}
