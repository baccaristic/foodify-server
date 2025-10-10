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
        boolean favorite,
        Long restaurantId,
        String restaurantName
) {}
