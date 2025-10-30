package com.foodify.server.modules.delivery.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DriverDailyFeePaymentRequest {
    @NotNull
    @Min(1)
    private Integer daysPaid;
}
