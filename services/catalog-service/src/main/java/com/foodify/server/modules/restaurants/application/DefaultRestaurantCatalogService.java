package com.foodify.server.modules.restaurants.application;

import com.foodify.server.modules.restaurants.domain.MenuItem;
import com.foodify.server.modules.restaurants.domain.MenuItemExtra;
import com.foodify.server.modules.restaurants.domain.Restaurant;
import com.foodify.server.modules.restaurants.repository.MenuItemExtraRepository;
import com.foodify.server.modules.restaurants.repository.MenuItemRepository;
import com.foodify.server.modules.restaurants.repository.RestaurantRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class DefaultRestaurantCatalogService implements RestaurantCatalogService {

    private final RestaurantRepository restaurantRepository;
    private final MenuItemRepository menuItemRepository;
    private final MenuItemExtraRepository menuItemExtraRepository;

    @Override
    public Restaurant getRestaurantOrThrow(Long restaurantId) {
        if (restaurantId == null) {
            throw new IllegalArgumentException("Restaurant id is required");
        }
        return restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new EntityNotFoundException("Restaurant not found"));
    }

    @Override
    public MenuItem getMenuItemOrThrow(Long menuItemId) {
        if (menuItemId == null) {
            throw new IllegalArgumentException("Menu item id is required");
        }
        return menuItemRepository.findById(menuItemId)
                .orElseThrow(() -> new EntityNotFoundException("Menu item not found"));
    }

    @Override
    public List<MenuItemExtra> getMenuItemExtras(List<Long> extraIds) {
        if (extraIds == null || extraIds.isEmpty()) {
            return Collections.emptyList();
        }
        return menuItemExtraRepository.findAllById(extraIds).stream()
                .filter(Objects::nonNull)
                .toList();
    }
}
