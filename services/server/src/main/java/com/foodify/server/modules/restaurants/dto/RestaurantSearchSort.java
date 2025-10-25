package com.foodify.server.modules.restaurants.dto;

import java.util.Locale;

public enum RestaurantSearchSort {
    PICKED,
    POPULAR,
    RATING;

    public static RestaurantSearchSort fromNullable(String value) {
        if (value == null || value.isBlank()) {
            return PICKED;
        }
        return switch (value.toLowerCase(Locale.ROOT)) {
            case "popular" -> POPULAR;
            case "rating" -> RATING;
            default -> PICKED;
        };
    }
}
