package com.foodify.server.modules.admin.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record DeliveryRatingRequest(
        @NotNull @Min(1) @Max(5) Integer timing,
        @NotNull @Min(1) @Max(5) Integer foodCondition,
        @NotNull @Min(1) @Max(5) Integer professionalism,
        @NotNull @Min(1) @Max(5) Integer overall,
        String comments
) {
}
