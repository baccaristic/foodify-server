package com.foodify.server.modules.rewards.application;

import com.foodify.server.modules.rewards.domain.Coupon;

import java.math.BigDecimal;

public record CouponApplicationResult(
        Coupon coupon,
        BigDecimal discount,
        BigDecimal deliveryFeeOverride
) {
}
