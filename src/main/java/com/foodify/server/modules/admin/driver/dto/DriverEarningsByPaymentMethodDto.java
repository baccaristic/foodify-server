package com.foodify.server.modules.admin.driver.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DriverEarningsByPaymentMethodDto {
    private Double cashEarnings;
    private Double cardEarnings;
    private Double totalEarnings;
    private Double totalCommission;
}
