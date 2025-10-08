package com.foodify.server.modules.restaurants.application.remote.dto;

import java.time.Instant;

public record CatalogRestaurantAvailabilityResponse(
        Long restaurantId,
        boolean available,
        Instant asOf,
        String openingHours,
        String closingHours
) {
}
