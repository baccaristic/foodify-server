package com.foodify.server.modules.delivery.dto;

import lombok.Data;

@Data
public class DriverLocationDto {
    private Long driverId;
    private double latitude;
    private double longitude;
}
