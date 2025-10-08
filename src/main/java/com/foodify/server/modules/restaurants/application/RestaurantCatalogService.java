package com.foodify.server.modules.restaurants.application;

import com.foodify.server.modules.restaurants.domain.MenuItem;
import com.foodify.server.modules.restaurants.domain.MenuItemExtra;
import com.foodify.server.modules.restaurants.domain.Restaurant;

import java.util.List;

public interface RestaurantCatalogService {

    Restaurant getRestaurantOrThrow(Long restaurantId);

    MenuItem getMenuItemOrThrow(Long menuItemId);

    List<MenuItemExtra> getMenuItemExtras(List<Long> extraIds);
}
