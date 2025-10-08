package com.foodify.catalogservice.api;

import java.time.Instant;

public record CatalogRestaurantAvailabilityDto(
        Long restaurantId,
        boolean available,
        Instant asOf,
        String openingHours,
        String closingHours
) {
}
