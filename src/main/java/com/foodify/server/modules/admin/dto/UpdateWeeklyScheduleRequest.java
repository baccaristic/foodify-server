package com.foodify.server.modules.admin.dto;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

public record UpdateWeeklyScheduleRequest(List<DaySchedule> days) {
    public record DaySchedule(
            DayOfWeek day,
            boolean open,
            LocalTime opensAt,
            LocalTime closesAt
    ) {
    }
}
