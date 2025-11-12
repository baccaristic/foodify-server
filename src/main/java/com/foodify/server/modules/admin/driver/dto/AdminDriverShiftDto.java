package com.foodify.server.modules.admin.driver.dto;

import com.foodify.server.modules.delivery.domain.DriverShiftStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminDriverShiftDto {
    private Long id;
    private Long driverId;
    private String driverName;
    private DriverShiftStatus status;
    private LocalDateTime startedAt;
    private LocalDateTime finishableAt;
    private LocalDateTime endedAt;
    private BigDecimal totalAmount;
    private BigDecimal driverShare;
    private BigDecimal restaurantShare;
    private boolean settled;
    private LocalDateTime settledAt;
    private List<AdminShiftDeliveryDto> deliveries;
}
