package com.foodify.catalogservice.api;

import java.time.Instant;

public record CatalogMenuItemPricingDto(
        Long menuItemId,
        double price,
        Double promotionPrice,
        String promotionLabel,
        Boolean promotionActive,
        Instant lastSynced
) {
}
