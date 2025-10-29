package com.foodify.server.modules.delivery.dto;

public record DriverRatingSummaryDto(
        Long driverId,
        long ratingCount,
        double timingAverage,
        double foodConditionAverage,
        double professionalismAverage,
        double overallAverage
) {
    public static DriverRatingSummaryDto empty(Long driverId) {
        return new DriverRatingSummaryDto(driverId, 0L, 0.0, 0.0, 0.0, 0.0);
    }
}
