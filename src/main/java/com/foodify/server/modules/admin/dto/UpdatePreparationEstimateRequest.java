package com.foodify.server.modules.admin.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record UpdatePreparationEstimateRequest(
        @NotNull(message = "Estimated minutes are required")
        @Min(value = 1, message = "Estimated minutes must be at least 1")
        Integer minutes
) {
}
