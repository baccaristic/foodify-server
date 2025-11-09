package com.foodify.server.modules.admin.driver.dto;

import com.foodify.server.modules.delivery.domain.DriverDepositStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DriverListItemDto {
    private Long id;
    private String name;
    private String phone;
    private DriverDepositStatus depositStatus;
}