package com.foodify.server.modules.restaurants.application;

import com.foodify.server.modules.restaurants.domain.MenuItem;
import com.foodify.server.modules.restaurants.domain.MenuItemExtra;
import com.foodify.server.modules.restaurants.domain.Restaurant;
import com.foodify.server.modules.restaurants.dto.MenuItemPricingDto;
import com.foodify.server.modules.restaurants.dto.PageResponse;
import com.foodify.server.modules.restaurants.dto.RestaurantAvailabilityDto;
import com.foodify.server.modules.restaurants.dto.RestaurantSearchItemDto;
import com.foodify.server.modules.restaurants.dto.RestaurantSearchQuery;

import java.util.List;
import java.time.Instant;

public interface RestaurantCatalogService {

    Restaurant getRestaurantOrThrow(Long restaurantId);

    MenuItem getMenuItemOrThrow(Long menuItemId);

    List<MenuItemExtra> getMenuItemExtras(List<Long> extraIds);

    PageResponse<RestaurantSearchItemDto> searchRestaurants(RestaurantSearchQuery query);

    RestaurantAvailabilityDto getRestaurantAvailability(Long restaurantId, Instant asOf);

    MenuItemPricingDto getMenuItemPricing(Long menuItemId);
}
