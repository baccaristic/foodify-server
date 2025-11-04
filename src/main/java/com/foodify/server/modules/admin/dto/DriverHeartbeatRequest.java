package com.foodify.server.modules.admin.dto;

import lombok.Data;

@Data
public class DriverHeartbeatRequest {
    private Double latitude;
    private Double longitude;
}
