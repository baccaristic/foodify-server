package com.foodify.server.modules.restaurants.sync;

public record CatalogMenuItemExtraSnapshot(
        Long id,
        Long menuItemId,
        String name,
        Double price,
        Boolean available
) {
}
