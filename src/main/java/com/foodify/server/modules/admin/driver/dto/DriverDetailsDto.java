package com.foodify.server.modules.admin.driver.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DriverDetailsDto {
    private Long id;
    private String email;
    private String name;
    private Boolean available;
    private String phone;
    private BigDecimal cashOnHand;
    private BigDecimal unpaidEarnings;
    private BigDecimal outstandingDailyFees;
    private Integer outstandingDailyFeeDays;
    private LocalDate lastDailyFeeDate;
}
