package com.foodify.server.modules.restaurants.dto;

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
        Integer page,
        Integer pageSize
) {
}
