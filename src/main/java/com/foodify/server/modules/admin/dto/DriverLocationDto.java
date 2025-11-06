package com.foodify.server.modules.admin.dto;

import lombok.Data;

@Data
public class DriverLocationDto {
    private Long driverId;
    private double latitude;
    private double longitude;
}
