package com.foodify.server.modules.restaurants.dto;

import java.util.List;

public record RestaurantSection(
        String displayType,
        List<RestaurantDisplayDto> restaurants
) {}
