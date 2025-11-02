package com.foodify.server.modules.orders.fee.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record UpdateServiceFeeRequest(
        @NotNull(message = "Service fee amount is required")
        @DecimalMin(value = "0.00", message = "Service fee must be zero or positive")
        @Digits(integer = 10, fraction = 2, message = "Service fee must have at most two decimal places")
        BigDecimal amount
) {
}
