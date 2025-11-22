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
public class ClientFilterRequest {
    private String query;
    private BigDecimal minDebt;
    private BigDecimal maxDebt;
    private BigDecimal minPoints;
    private BigDecimal maxPoints;
    private Boolean active;
}
