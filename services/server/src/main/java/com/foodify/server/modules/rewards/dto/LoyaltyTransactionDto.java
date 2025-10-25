package com.foodify.server.modules.rewards.dto;

import com.foodify.server.modules.rewards.domain.LoyaltyPointTransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record LoyaltyTransactionDto(
        Long id,
        LoyaltyPointTransactionType type,
        BigDecimal points,
        String description,
        LocalDateTime createdAt
) {
}
