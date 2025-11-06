package com.foodify.server.modules.restaurants.dto;

import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;
import java.time.LocalDate;

@Value
@Builder
public class DailyRevenueDto {
    LocalDate date;
    BigDecimal revenue;
    Long orderCount;
}
