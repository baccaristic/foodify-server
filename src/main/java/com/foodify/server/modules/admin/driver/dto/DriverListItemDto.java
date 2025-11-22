package com.foodify.server.modules.admin.driver.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DriverListItemDto {
    private Long id;
    private String name;
    private String phone;
    private boolean paid;
}