package com.foodify.server.modules.admin.client.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminClientDto {
    private Long id;
    private String name;
    private String phoneNumber;
    private String email;
    private BigDecimal points;
    private boolean active;
    private BigDecimal debt;
}
