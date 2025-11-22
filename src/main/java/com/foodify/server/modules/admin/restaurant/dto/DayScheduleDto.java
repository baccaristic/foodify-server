package com.foodify.server.modules.admin.restaurant.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DayScheduleDto {
    private LocalTime opensAt;
    private LocalTime closesAt;
    private boolean open;
}