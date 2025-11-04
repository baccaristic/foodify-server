package com.foodify.server.modules.admin.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class DeliveryMetricsDto {
    Double acceptanceRate; // percentage
    Double completionRate; // percentage
    Double onTimeRate; // percentage
    Double avgDeliveryDuration; // in minutes
    Long totalOffered;
    Long totalAccepted;
    Long totalCompleted;
    Long totalOnTime;
}
