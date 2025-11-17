package com.foodify.server.modules.restaurants.dto;

import com.foodify.server.modules.restaurants.domain.Restaurant;
import lombok.Getter;

/**
 * Context class that holds information about a restaurant user (either admin or cashier)
 * and their associated restaurant.
 */
@Getter
public class RestaurantUserContext {
    private final Long userId;
    private final Restaurant restaurant;

    public RestaurantUserContext(Long userId, Restaurant restaurant) {
        this.userId = userId;
        this.restaurant = restaurant;
    }
}
