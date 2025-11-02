package com.foodify.server.modules.orders.fee.dto;

import com.foodify.server.modules.orders.fee.domain.ServiceFeeSetting;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

public record ServiceFeeResponse(
        BigDecimal amount,
        LocalDateTime updatedAt,
        String updatedBy
) {
    public static ServiceFeeResponse fromSetting(ServiceFeeSetting setting) {
        if (setting == null) {
            BigDecimal zero = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
            return new ServiceFeeResponse(zero, null, null);
        }

        BigDecimal normalizedAmount = setting.getAmount() == null
                ? BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP)
                : setting.getAmount().setScale(2, RoundingMode.HALF_UP);
        return new ServiceFeeResponse(normalizedAmount, setting.getUpdatedAt(), setting.getUpdatedBy());
    }
}
