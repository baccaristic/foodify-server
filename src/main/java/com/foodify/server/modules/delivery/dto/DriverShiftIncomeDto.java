package com.foodify.server.modules.delivery.dto;

import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;

@Value
@Builder
public class DriverShiftIncomeDto {
    Long id;
    String startTime;
    String endTime;
    BigDecimal total;
}
