package com.foodify.server.modules.delivery.dto;

import com.foodify.server.modules.delivery.domain.DriverShiftStatus;
import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;

@Value
@Builder
public class DriverShiftDto {
    DriverShiftStatus status;
    LocalDateTime startedAt;
    LocalDateTime finishableAt;
    LocalDateTime endedAt;
}
