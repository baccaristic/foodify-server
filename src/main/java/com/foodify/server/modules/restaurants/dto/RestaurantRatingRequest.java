package com.foodify.server.modules.restaurants.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RestaurantRatingRequest(
        @NotNull Boolean thumbsUp,
        @Size(max = 1024) String comments
) {
}
