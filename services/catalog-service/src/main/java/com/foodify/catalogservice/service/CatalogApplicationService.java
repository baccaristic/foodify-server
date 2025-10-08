package com.foodify.catalogservice.service;

import com.foodify.catalogservice.domain.MenuItem;
import com.foodify.catalogservice.domain.MenuItemExtra;
import com.foodify.catalogservice.domain.Restaurant;
import com.foodify.catalogservice.repository.MenuItemExtraRepository;
import com.foodify.catalogservice.repository.MenuItemRepository;
import com.foodify.catalogservice.repository.RestaurantRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CatalogApplicationService {

    private final RestaurantRepository restaurantRepository;
    private final MenuItemRepository menuItemRepository;
    private final MenuItemExtraRepository menuItemExtraRepository;

    @Transactional(readOnly = true)
    public Restaurant getRestaurantOrThrow(Long restaurantId) {
        return restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new EntityNotFoundException("Restaurant not found"));
    }

    @Transactional(readOnly = true)
    public MenuItem getMenuItemOrThrow(Long menuItemId) {
        return menuItemRepository.findById(menuItemId)
                .orElseThrow(() -> new EntityNotFoundException("Menu item not found"));
    }

    @Transactional(readOnly = true)
    public List<MenuItemExtra> getExtras(List<Long> extraIds) {
        if (CollectionUtils.isEmpty(extraIds)) {
            return Collections.emptyList();
        }
        return menuItemExtraRepository.findAllById(extraIds).stream()
                .filter(Objects::nonNull)
                .toList();
    }
}
