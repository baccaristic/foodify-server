package com.foodify.server.modules.admin.dto;

import com.foodify.server.modules.delivery.domain.DriverShiftStatus;
import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Value
@Builder
public class DriverShiftDto {
    DriverShiftStatus status;
    LocalDateTime startedAt;
    LocalDateTime finishableAt;
    LocalDateTime endedAt;
    BigDecimal totalAmount;
    BigDecimal driverShare;
    BigDecimal restaurantShare;
    boolean settled;
    LocalDateTime settledAt;
}
