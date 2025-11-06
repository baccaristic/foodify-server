package com.foodify.server.modules.admin.dto;

import com.foodify.server.modules.delivery.domain.DriverDepositStatus;
import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Value
@Builder
public class DriverDepositDto {
    Long id;
    BigDecimal depositAmount;
    BigDecimal earningsPaid;
    BigDecimal feesDeducted;
    DriverDepositStatus status;
    LocalDateTime createdAt;
    LocalDateTime confirmedAt;
}
