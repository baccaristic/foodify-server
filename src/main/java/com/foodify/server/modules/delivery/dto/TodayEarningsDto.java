package com.foodify.server.modules.delivery.dto;

import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;

@Value
@Builder
public class TodayEarningsDto {
    BigDecimal totalEarnings;
    BigDecimal cashEarnings;
    BigDecimal cardEarnings;
    BigDecimal commission;
    Integer numberOfDeliveries;
}
