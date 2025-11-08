package com.foodify.server.modules.restaurants.dto;

import com.foodify.server.modules.restaurants.domain.RestaurantCategory;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

/**
 * Query parameters for restaurant search.
 * Includes optional clientDate and clientTime to determine restaurant operating status
 * based on the client's local time (which should match the restaurant's timezone).
 */
public record RestaurantSearchQuery(
        String query,
        Boolean hasPromotion,
        Boolean isTopChoice,
        Boolean hasFreeDelivery,
        RestaurantSearchSort sort,
        Boolean topEatOnly,
        Double maxDeliveryFee,
        Double clientLatitude,
        Double clientLongitude,
        Set<RestaurantCategory> categories,
        Integer page,
        Integer pageSize,
        LocalDate clientDate,
        LocalTime clientTime
) {
}
