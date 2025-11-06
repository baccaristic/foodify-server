package com.foodify.server.modules.admin.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;

@Value
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DriverEarningsSummaryDto {
    BigDecimal avilableBalance;
    BigDecimal todayBalance;
    BigDecimal weekBalance;
    BigDecimal monthBalance;
    BigDecimal totalEarnings;
}
