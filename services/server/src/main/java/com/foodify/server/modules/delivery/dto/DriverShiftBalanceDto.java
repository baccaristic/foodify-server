package com.foodify.server.modules.delivery.dto;

import lombok.Value;

import java.math.BigDecimal;

@Value
public class DriverShiftBalanceDto {
    BigDecimal currentTotal;
}
