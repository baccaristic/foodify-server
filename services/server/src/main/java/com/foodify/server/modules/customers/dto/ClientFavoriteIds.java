package com.foodify.server.modules.customers.dto;

import java.util.Set;

public record ClientFavoriteIds(
        Set<Long> restaurantIds,
        Set<Long> menuItemIds
) {
}
