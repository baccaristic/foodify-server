package com.foodify.server.modules.restaurants.application;

import com.foodify.server.modules.restaurants.application.remote.dto.CatalogMenuItemExtraResponse;
import com.foodify.server.modules.restaurants.application.remote.dto.CatalogMenuItemResponse;
import com.foodify.server.modules.restaurants.application.remote.dto.CatalogRestaurantResponse;
import com.foodify.server.modules.restaurants.domain.MenuItem;
import com.foodify.server.modules.restaurants.domain.MenuItemExtra;
import com.foodify.server.modules.restaurants.domain.Restaurant;
import com.foodify.server.modules.restaurants.repository.MenuItemExtraRepository;
import com.foodify.server.modules.restaurants.repository.MenuItemRepository;
import com.foodify.server.modules.restaurants.repository.RestaurantRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestClient;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class RemoteRestaurantCatalogService implements RestaurantCatalogService {

    private static final Logger log = LoggerFactory.getLogger(RemoteRestaurantCatalogService.class);

    private final RestClient catalogRestClient;
    private final RestaurantRepository restaurantRepository;
    private final MenuItemRepository menuItemRepository;
    private final MenuItemExtraRepository menuItemExtraRepository;

    @Override
    public Restaurant getRestaurantOrThrow(Long restaurantId) {
        CatalogRestaurantResponse response = catalogRestClient.get()
                .uri("/api/catalog/restaurants/{restaurantId}", restaurantId)
                .retrieve()
                .body(CatalogRestaurantResponse.class);

        if (response == null) {
            throw new EntityNotFoundException("Restaurant not found");
        }

        return restaurantRepository.findById(response.id())
                .orElseThrow(() -> new EntityNotFoundException("Restaurant " + restaurantId + " not synchronized locally"));
    }

    @Override
    public MenuItem getMenuItemOrThrow(Long menuItemId) {
        CatalogMenuItemResponse response = catalogRestClient.get()
                .uri("/api/catalog/menu-items/{menuItemId}", menuItemId)
                .retrieve()
                .body(CatalogMenuItemResponse.class);

        if (response == null) {
            throw new EntityNotFoundException("Menu item not found");
        }

        MenuItem menuItem = menuItemRepository.findById(response.id())
                .orElseThrow(() -> new EntityNotFoundException("Menu item " + menuItemId + " not synchronized locally"));

        if (!Objects.equals(menuItem.getRestaurant().getId(), response.restaurantId())) {
            log.warn("Menu item {} references restaurant {} locally but remote catalog reported {}", menuItemId,
                    menuItem.getRestaurant().getId(), response.restaurantId());
        }

        return menuItem;
    }

    @Override
    public List<MenuItemExtra> getMenuItemExtras(List<Long> extraIds) {
        if (CollectionUtils.isEmpty(extraIds)) {
            return Collections.emptyList();
        }

        CatalogMenuItemExtraResponse[] response = catalogRestClient.post()
                .uri("/api/catalog/menu-item-extras/_lookup")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Map.of("ids", extraIds))
                .retrieve()
                .body(CatalogMenuItemExtraResponse[].class);

        if (response == null || response.length == 0) {
            return Collections.emptyList();
        }

        List<Long> requestedIds = Arrays.stream(response)
                .map(CatalogMenuItemExtraResponse::id)
                .collect(Collectors.toList());

        if (!requestedIds.containsAll(extraIds)) {
            log.warn("Remote catalog returned subset of requested extras. Requested={}, received={}", extraIds, requestedIds);
        }

        return menuItemExtraRepository.findAllById(extraIds).stream()
                .filter(Objects::nonNull)
                .toList();
    }
}
