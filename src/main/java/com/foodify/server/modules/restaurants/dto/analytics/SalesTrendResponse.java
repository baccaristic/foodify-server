package com.foodify.server.modules.restaurants.dto.analytics;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record SalesTrendResponse(
        AnalyticsPeriod period,
        List<DataPoint> data
) {
    public record DataPoint(
            LocalDate date,
            BigDecimal revenue,
            Long orderCount
    ) {}
}
