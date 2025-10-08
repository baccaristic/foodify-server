package com.foodify.server.modules.restaurants.application;

import com.foodify.server.modules.restaurants.dto.PageResponse;
import com.foodify.server.modules.restaurants.dto.RestaurantSearchItemDto;
import com.foodify.server.modules.restaurants.dto.RestaurantSearchQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RestaurantSearchService {

    private final RestaurantCatalogService restaurantCatalogService;

    public PageResponse<RestaurantSearchItemDto> search(RestaurantSearchQuery query) {
        return restaurantCatalogService.searchRestaurants(query);
    }
}
