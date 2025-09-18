package com.foodify.server.dto;

import lombok.Data;

@Data
public class DriverLocationDto {
    private Long driverId;
    private double latitude;
    private double longitude;
}
