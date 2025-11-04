package com.foodify.server.modules.restaurants.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class RestaurantTotalOrdersDto {
    Long todayOrders;
    Long yesterdayOrders;
    Double percentageChange;
}
