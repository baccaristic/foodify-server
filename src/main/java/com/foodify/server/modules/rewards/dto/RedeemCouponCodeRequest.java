package com.foodify.server.modules.rewards.dto;

public class RedeemCouponCodeRequest {
    private String couponCode;

    public String getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
    }
}
