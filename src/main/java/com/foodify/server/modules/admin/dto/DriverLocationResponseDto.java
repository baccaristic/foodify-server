package com.foodify.server.modules.admin.dto;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;

@Value
@Builder
public class DriverLocationResponseDto {
    Long driverId;
    Double latitude;
    Double longitude;
    LocalDateTime lastUpdated;
}
