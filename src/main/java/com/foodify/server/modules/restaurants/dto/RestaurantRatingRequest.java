package com.foodify.server.modules.restaurants.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RestaurantRatingRequest(
        @NotNull @Min(1) @Max(5) Integer rating,
        @Size(max = 1024) String comments
) {
}
