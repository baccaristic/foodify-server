package com.foodify.server.modules.restaurants.dto;

import java.time.Instant;

public record MenuItemPricingDto(
        Long menuItemId,
        double price,
        Double promotionPrice,
        String promotionLabel,
        Boolean promotionActive,
        Instant lastSynced
) {
}
