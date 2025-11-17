package com.foodify.server.modules.restaurants.dto.analytics;

import java.util.List;

public record TopDishesResponse(
        AnalyticsPeriod period,
        List<DishData> topDishes
) {
    public record DishData(
            Long menuItemId,
            String menuItemName,
            Long orderCount,
            Long quantitySold
    ) {}
}
