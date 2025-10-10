package com.foodify.server.modules.customers.dto;

public record MenuItemFavoriteDto(
        Long id,
        String name,
        String description,
        double price,
        String imageUrl,
        boolean popular,
        Double promotionPrice,
        String promotionLabel,
        boolean promotionActive,
        Long restaurantId,
        String restaurantName
) {}
