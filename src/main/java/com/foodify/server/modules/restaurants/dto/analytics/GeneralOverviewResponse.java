package com.foodify.server.modules.restaurants.dto.analytics;

import java.math.BigDecimal;

public record GeneralOverviewResponse(
        AnalyticsPeriod period,
        RevenueMetric revenue,
        OrdersMetric orders,
        PreparationTimeMetric preparationTime,
        RatingMetric rating
) {
    public record RevenueMetric(
            BigDecimal current,
            BigDecimal previous,
            BigDecimal change,
            BigDecimal percentageChange
    ) {}

    public record OrdersMetric(
            Long current,
            Long previous,
            Long change,
            BigDecimal percentageChange
    ) {}

    public record PreparationTimeMetric(
            BigDecimal currentMinutes,
            BigDecimal previousMinutes,
            BigDecimal changeMinutes,
            BigDecimal percentageChange
    ) {}

    public record RatingMetric(
            BigDecimal currentStars,
            BigDecimal previousStars,
            BigDecimal changeStars,
            BigDecimal percentageChange
    ) {}
}
