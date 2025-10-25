package com.foodify.server.modules.orders.dto;

import java.math.BigDecimal;
import java.util.List;

public record OrderItemDTO(
        Long menuItemId,
        String menuItemName,
        int quantity,
        List<String> extras,
        String specialInstructions,
        BigDecimal unitBasePrice,
        BigDecimal unitPrice,
        BigDecimal unitExtrasPrice,
        BigDecimal lineSubtotal,
        BigDecimal promotionDiscount,
        BigDecimal lineItemsTotal,
        BigDecimal extrasTotal,
        BigDecimal lineTotal
) {}
