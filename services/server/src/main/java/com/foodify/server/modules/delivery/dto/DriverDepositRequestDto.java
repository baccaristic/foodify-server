package com.foodify.server.modules.delivery.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class DriverDepositRequestDto {
    private BigDecimal amount;
}
