package com.foodify.server.modules.admin.dto;

import lombok.Value;

import java.math.BigDecimal;

@Value
public class DriverShiftBalanceDto {
    BigDecimal currentTotal;
}
