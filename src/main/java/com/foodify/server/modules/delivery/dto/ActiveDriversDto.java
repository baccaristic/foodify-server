package com.foodify.server.modules.delivery.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ActiveDriversDto {
    Long activeDriversCount;
    Long totalDriversCount;
}
