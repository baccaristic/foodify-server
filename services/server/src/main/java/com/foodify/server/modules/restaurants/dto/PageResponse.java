package com.foodify.server.modules.restaurants.dto;

import java.util.List;

public record PageResponse<T>(
        List<T> items,
        int page,
        int pageSize,
        long totalItems
) {
}
