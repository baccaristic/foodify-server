package com.foodify.server.modules.admin.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;
import java.util.List;

@Value
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DriverShiftIncomeResponseDto {
    BigDecimal total;
    List<DriverShiftIncomeDto> shifts;
}
