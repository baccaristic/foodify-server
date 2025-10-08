package com.foodify.catalogservice.api;

public record CatalogMenuItemExtraDto(
        Long id,
        Long optionGroupId,
        Long menuItemId,
        String name,
        double price,
        boolean isDefault
) {
}
