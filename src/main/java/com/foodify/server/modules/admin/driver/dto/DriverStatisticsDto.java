package com.foodify.server.modules.admin.driver.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DriverStatisticsDto {
    private Long driverId;
    private Double averageRating;
    private Double onTimePercentage;
    private Double averageDeliveryTimeMinutes;
}
