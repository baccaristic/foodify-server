package com.foodify.server.modules.restaurants.dto;

import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;

@Value
@Builder
public class RestaurantAvgOrderValueDto {
    BigDecimal todayAvgOrderValue;
    BigDecimal yesterdayAvgOrderValue;
    Double percentageChange;
}
