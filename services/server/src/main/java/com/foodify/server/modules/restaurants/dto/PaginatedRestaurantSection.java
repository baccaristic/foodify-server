package com.foodify.server.modules.restaurants.dto;

import java.util.List;

public record PaginatedRestaurantSection(
        String displayType,
        List<RestaurantDisplayDto> restaurants,
        int page,
        int pageSize,
        long totalElements
) {}
