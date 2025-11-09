package com.foodify.server.modules.admin.driver.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DailyRatingDto {
    private LocalDate date;
    private Double avgRating;
}