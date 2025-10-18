package com.foodify.server.modules.restaurants.dto;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public record OperatingHoursResponse(
        List<WeeklyScheduleEntry> weeklySchedule,
        List<SpecialDay> specialDays
) {
    public record WeeklyScheduleEntry(
            DayOfWeek day,
            boolean open,
            LocalTime opensAt,
            LocalTime closesAt
    ) {
    }

    public record SpecialDay(
            Long id,
            String name,
            LocalDate date,
            boolean open,
            LocalTime opensAt,
            LocalTime closesAt
    ) {
    }
}
