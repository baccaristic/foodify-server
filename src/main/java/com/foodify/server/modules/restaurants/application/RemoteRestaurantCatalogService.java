package com.foodify.server.modules.restaurants.application;

import com.foodify.server.modules.restaurants.application.remote.dto.CatalogMenuItemExtraResponse;
import com.foodify.server.modules.restaurants.application.remote.dto.CatalogMenuItemPricingResponse;
import com.foodify.server.modules.restaurants.application.remote.dto.CatalogMenuItemPromotionResponse;
import com.foodify.server.modules.restaurants.application.remote.dto.CatalogMenuItemResponse;
import com.foodify.server.modules.restaurants.application.remote.dto.CatalogRestaurantAvailabilityResponse;
import com.foodify.server.modules.restaurants.application.remote.dto.CatalogRestaurantResponse;
import com.foodify.server.modules.restaurants.application.remote.dto.CatalogRestaurantSearchItemResponse;
import com.foodify.server.modules.restaurants.application.remote.dto.CatalogRestaurantSearchResponse;
import com.foodify.server.modules.restaurants.domain.MenuItem;
import com.foodify.server.modules.restaurants.domain.MenuItemExtra;
import com.foodify.server.modules.restaurants.domain.Restaurant;
import com.foodify.server.modules.restaurants.dto.MenuItemPricingDto;
import com.foodify.server.modules.restaurants.dto.MenuItemPromotionDto;
import com.foodify.server.modules.restaurants.dto.PageResponse;
import com.foodify.server.modules.restaurants.dto.RestaurantAvailabilityDto;
import com.foodify.server.modules.restaurants.dto.RestaurantSearchItemDto;
import com.foodify.server.modules.restaurants.dto.RestaurantSearchQuery;
import com.foodify.server.modules.restaurants.dto.RestaurantSearchSort;
import com.foodify.server.modules.restaurants.repository.MenuItemExtraRepository;
import com.foodify.server.modules.restaurants.repository.MenuItemRepository;
import com.foodify.server.modules.restaurants.repository.RestaurantRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.util.CollectionUtils;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.client.RestClient;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.time.Instant;

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

    @Override
    public PageResponse<RestaurantSearchItemDto> searchRestaurants(RestaurantSearchQuery query) {
        int page = query != null && query.page() != null && query.page() > 0 ? query.page() : 1;
        int pageSize = query != null && query.pageSize() != null && query.pageSize() > 0 ? query.pageSize() : 20;
        RestaurantSearchSort sort = query != null ? query.sort() : null;

        CatalogRestaurantSearchResponse response = catalogRestClient.get()
                .uri(uriBuilder -> buildSearchUri(uriBuilder, query, page, pageSize, sort))
                .retrieve()
                .body(CatalogRestaurantSearchResponse.class);

        if (response == null) {
            return new PageResponse<>(List.of(), page, pageSize, 0);
        }

        List<RestaurantSearchItemDto> items = response.items() == null ? List.of()
                : response.items().stream()
                .map(this::toSearchItemDto)
                .toList();

        return new PageResponse<>(items, response.page(), response.pageSize(), response.totalItems());
    }

    @Override
    public RestaurantAvailabilityDto getRestaurantAvailability(Long restaurantId, Instant asOf) {
        CatalogRestaurantAvailabilityResponse response = catalogRestClient.get()
                .uri(uriBuilder -> buildAvailabilityUri(uriBuilder, restaurantId, asOf))
                .retrieve()
                .body(CatalogRestaurantAvailabilityResponse.class);

        if (response == null) {
            Instant effective = asOf != null ? asOf : Instant.now();
            return new RestaurantAvailabilityDto(restaurantId, true, effective, null, null);
        }

        return new RestaurantAvailabilityDto(
                response.restaurantId(),
                response.available(),
                response.asOf(),
                response.openingHours(),
                response.closingHours()
        );
    }

    @Override
    public MenuItemPricingDto getMenuItemPricing(Long menuItemId) {
        CatalogMenuItemPricingResponse response = catalogRestClient.get()
                .uri("/api/catalog/menu-items/{menuItemId}/pricing", menuItemId)
                .retrieve()
                .body(CatalogMenuItemPricingResponse.class);

        if (response == null) {
            MenuItem menuItem = menuItemRepository.findById(menuItemId)
                    .orElseThrow(() -> new EntityNotFoundException("Menu item " + menuItemId + " not synchronized locally"));
            return new MenuItemPricingDto(
                    menuItem.getId(),
                    menuItem.getPrice(),
                    menuItem.getPromotionPrice(),
                    menuItem.getPromotionLabel(),
                    menuItem.getPromotionActive(),
                    Instant.now()
            );
        }

        return new MenuItemPricingDto(
                response.menuItemId(),
                response.price(),
                response.promotionPrice(),
                response.promotionLabel(),
                response.promotionActive(),
                response.lastSynced()
        );
    }

    private RestaurantSearchItemDto toSearchItemDto(CatalogRestaurantSearchItemResponse item) {
        List<MenuItemPromotionDto> promotions = item.promotedMenuItems() == null ? List.of()
                : item.promotedMenuItems().stream()
                .map(this::toPromotionDto)
                .toList();
        return new RestaurantSearchItemDto(
                item.id(),
                item.name(),
                item.deliveryTimeRange(),
                item.rating(),
                item.topChoice(),
                item.freeDelivery(),
                item.imageUrl(),
                promotions
        );
    }

    private MenuItemPromotionDto toPromotionDto(CatalogMenuItemPromotionResponse promotion) {
        return new MenuItemPromotionDto(
                promotion.id(),
                promotion.name(),
                promotion.price(),
                promotion.promotionPrice(),
                promotion.promotionLabel(),
                promotion.imageUrl()
        );
    }

    private java.net.URI buildSearchUri(UriBuilder uriBuilder, RestaurantSearchQuery query, int page, int pageSize, RestaurantSearchSort sort) {
        UriBuilder builder = uriBuilder.path("/api/catalog/restaurants/_search")
                .queryParam("page", page)
                .queryParam("pageSize", pageSize);
        if (query != null) {
            if (query.query() != null && !query.query().isBlank()) {
                builder.queryParam("query", query.query());
            }
            if (Boolean.TRUE.equals(query.hasPromotion())) {
                builder.queryParam("hasPromotion", true);
            }
            if (Boolean.TRUE.equals(query.isTopChoice())) {
                builder.queryParam("isTopChoice", true);
            }
            if (Boolean.TRUE.equals(query.hasFreeDelivery())) {
                builder.queryParam("hasFreeDelivery", true);
            }
            if (Boolean.TRUE.equals(query.topEatOnly())) {
                builder.queryParam("topEatOnly", true);
            }
            if (query.maxDeliveryFee() != null) {
                builder.queryParam("maxDeliveryFee", query.maxDeliveryFee());
            }
        }
        RestaurantSearchSort effectiveSort = sort == null ? RestaurantSearchSort.PICKED : sort;
        builder.queryParam("sort", effectiveSort.name());
        return builder.build();
    }

    private java.net.URI buildAvailabilityUri(UriBuilder uriBuilder, Long restaurantId, Instant asOf) {
        UriBuilder builder = uriBuilder
                .path("/api/catalog/restaurants/{restaurantId}/availability");
        if (asOf != null) {
            builder.queryParam("at", asOf.toString());
        }
        return builder.build(restaurantId);
    }
}
