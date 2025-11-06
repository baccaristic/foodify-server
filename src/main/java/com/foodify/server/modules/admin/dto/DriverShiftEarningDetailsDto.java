package com.foodify.server.modules.admin.dto;

import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;
import java.util.List;

@Value
@Builder
public class DriverShiftEarningDetailsDto {
    Long shiftId;
    String from;
    String to;
    BigDecimal total;
    String date;
    List<DriverShiftOrderEarningDto> orders;
}
