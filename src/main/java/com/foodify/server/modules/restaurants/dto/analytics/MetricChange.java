package com.foodify.server.modules.restaurants.dto.analytics;

import java.math.BigDecimal;

public record MetricChange(
        BigDecimal current,
        BigDecimal previous,
        BigDecimal change,
        BigDecimal percentageChange
) {
}
