package com.foodify.server.modules.rewards.dto;

import java.math.BigDecimal;

public record LoyaltyBalanceResponse(
        BigDecimal balance,
        BigDecimal lifetimeEarned,
        BigDecimal lifetimeRedeemed
) {
}
