package com.foodify.server.modules.rewards.dto;

import com.foodify.server.modules.rewards.domain.CouponType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CouponDto(
        String code,
        CouponType type,
        BigDecimal discountPercent,
        boolean publicCoupon,
        boolean redeemed,
        boolean active,
        boolean createdFromPoints,
        LocalDateTime assignedAt
) {
}
