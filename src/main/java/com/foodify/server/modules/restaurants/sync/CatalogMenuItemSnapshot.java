package com.foodify.server.modules.restaurants.sync;

public record CatalogMenuItemSnapshot(
        Long id,
        Long restaurantId,
        Double price,
        Double promotionPrice,
        Boolean promotionActive,
        Boolean available
) {
}
