package com.foodify.server.modules.admin.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ActiveDriversDto {
    Long activeDriversCount;
    Long totalDriversCount;
}
