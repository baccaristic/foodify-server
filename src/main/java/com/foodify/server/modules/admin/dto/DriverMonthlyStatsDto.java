package com.foodify.server.modules.admin.dto;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDate;
import java.util.List;

@Value
@Builder
public class DriverMonthlyStatsDto {
    List<DailyStatDto> dailyStats;

    @Value
    @Builder
    public static class DailyStatDto {
        LocalDate date;
        Double averageRating;
        Double onTimePercentage;
    }
}
