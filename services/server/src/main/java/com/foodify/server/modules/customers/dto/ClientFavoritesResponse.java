package com.foodify.server.modules.customers.dto;

import com.foodify.server.modules.restaurants.dto.RestaurantDisplayDto;

import java.util.List;

public record ClientFavoritesResponse(
        List<RestaurantDisplayDto> restaurants,
        List<MenuItemFavoriteDto> menuItems
) {}
