package com.foodify.server.modules.admin.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class RestaurantTotalOrdersDto {
    Long todayOrders;
    Long yesterdayOrders;
    Double percentageChange;
}
