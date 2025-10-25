package com.foodify.server.modules.restaurants.dto;

public record NearbyRestaurantsResponse(
        RestaurantSection topPicks,
        RestaurantSection orderAgain,
        RestaurantSection promotions,
        PaginatedRestaurantSection others
) {}
