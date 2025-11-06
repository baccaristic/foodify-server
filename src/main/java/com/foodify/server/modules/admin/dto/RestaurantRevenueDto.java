package com.foodify.server.modules.admin.dto;

import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;

@Value
@Builder
public class RestaurantRevenueDto {
    BigDecimal todayRevenue;
    BigDecimal yesterdayRevenue;
    Double percentageChange;
}
