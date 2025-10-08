package com.foodify.server.modules.restaurants.application;

import com.foodify.server.modules.restaurants.domain.MenuItem;
import com.foodify.server.modules.restaurants.domain.MenuItemExtra;
import com.foodify.server.modules.restaurants.domain.Restaurant;
import com.foodify.server.modules.restaurants.dto.MenuItemPricingDto;
import com.foodify.server.modules.restaurants.dto.PageResponse;
import com.foodify.server.modules.restaurants.dto.RestaurantAvailabilityDto;
import com.foodify.server.modules.restaurants.dto.RestaurantSearchItemDto;
import com.foodify.server.modules.restaurants.dto.RestaurantSearchQuery;
import com.foodify.server.modules.restaurants.repository.MenuItemExtraRepository;
import com.foodify.server.modules.restaurants.repository.MenuItemRepository;
import com.foodify.server.modules.restaurants.repository.RestaurantRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class DefaultRestaurantCatalogService implements RestaurantCatalogService {

    private final LocalRestaurantCatalogService delegate;

    public DefaultRestaurantCatalogService(RestaurantRepository restaurantRepository,
                                           MenuItemRepository menuItemRepository,
                                           MenuItemExtraRepository menuItemExtraRepository) {
        this.delegate = new LocalRestaurantCatalogService(restaurantRepository, menuItemRepository, menuItemExtraRepository);
    }

    @Override
    public Restaurant getRestaurantOrThrow(Long restaurantId) {
        return delegate.getRestaurantOrThrow(restaurantId);
    }

    @Override
    public MenuItem getMenuItemOrThrow(Long menuItemId) {
        return delegate.getMenuItemOrThrow(menuItemId);
    }

    @Override
    public List<MenuItemExtra> getMenuItemExtras(List<Long> extraIds) {
        return delegate.getMenuItemExtras(extraIds);
    }

    @Override
    public PageResponse<RestaurantSearchItemDto> searchRestaurants(RestaurantSearchQuery query) {
        return delegate.searchRestaurants(query);
    }

    @Override
    public RestaurantAvailabilityDto getRestaurantAvailability(Long restaurantId, Instant asOf) {
        return delegate.getRestaurantAvailability(restaurantId, asOf);
    }

    @Override
    public MenuItemPricingDto getMenuItemPricing(Long menuItemId) {
        return delegate.getMenuItemPricing(menuItemId);
    }
}
