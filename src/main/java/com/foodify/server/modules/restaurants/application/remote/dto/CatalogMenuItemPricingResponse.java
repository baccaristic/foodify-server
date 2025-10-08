package com.foodify.server.modules.restaurants.application.remote.dto;

import java.time.Instant;

public record CatalogMenuItemPricingResponse(
        Long menuItemId,
        double price,
        Double promotionPrice,
        String promotionLabel,
        Boolean promotionActive,
        Instant lastSynced
) {
}
