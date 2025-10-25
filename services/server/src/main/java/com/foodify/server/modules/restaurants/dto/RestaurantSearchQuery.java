package com.foodify.server.modules.restaurants.dto;

import com.foodify.server.modules.restaurants.domain.RestaurantCategory;

import java.util.Set;

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
        Integer pageSize
) {
}
