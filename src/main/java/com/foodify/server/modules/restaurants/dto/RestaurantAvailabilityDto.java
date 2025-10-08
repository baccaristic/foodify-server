package com.foodify.server.modules.restaurants.dto;

import java.time.Instant;

public record RestaurantAvailabilityDto(
        Long restaurantId,
        boolean available,
        Instant asOf,
        String openingHours,
        String closingHours
) {
}
