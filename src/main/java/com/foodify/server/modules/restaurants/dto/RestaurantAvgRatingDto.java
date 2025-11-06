package com.foodify.server.modules.restaurants.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class RestaurantAvgRatingDto {
    Double todayAvgRating;
    Double yesterdayAvgRating;
    Double percentageChange;
    Long todayRatingCount;
    Long yesterdayRatingCount;
}
