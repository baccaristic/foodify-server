package com.foodify.server.modules.restaurants.application;

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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.time.Instant;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class LocalRestaurantCatalogService implements RestaurantCatalogService {

    private final RestaurantRepository restaurantRepository;
    private final MenuItemRepository menuItemRepository;
    private final MenuItemExtraRepository menuItemExtraRepository;
    private final ConcurrentMap<Long, MenuItemPricingDto> pricingCache = new ConcurrentHashMap<>();

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

    @Override
    public PageResponse<RestaurantSearchItemDto> searchRestaurants(RestaurantSearchQuery query) {
        Specification<Restaurant> specification = buildSpecification(query);
        Sort sort = toSort(query != null ? query.sort() : null);
        int page = query != null && query.page() != null && query.page() > 0 ? query.page() : 1;
        int pageSize = query != null && query.pageSize() != null && query.pageSize() > 0 ? query.pageSize() : 20;
        Pageable pageable = PageRequest.of(Math.max(page - 1, 0), pageSize, sort);

        Page<Restaurant> restaurants = restaurantRepository.findAll(specification, pageable);
        List<Restaurant> content = restaurants.getContent();
        Map<Long, List<MenuItem>> promotionsByRestaurant = groupPromotedItems(content);
        List<RestaurantSearchItemDto> items = content.stream()
                .map(restaurant -> toDto(restaurant, promotionsByRestaurant.getOrDefault(restaurant.getId(), List.of())))
                .toList();

        return new PageResponse<>(items, page, pageSize, restaurants.getTotalElements());
    }

    @Override
    public RestaurantAvailabilityDto getRestaurantAvailability(Long restaurantId, Instant asOf) {
        Restaurant restaurant = getRestaurantOrThrow(restaurantId);
        Instant effective = Optional.ofNullable(asOf).orElse(Instant.now());
        LocalTime opening = parseTime(restaurant.getOpeningHours());
        LocalTime closing = parseTime(restaurant.getClosingHours());
        boolean available = isWithinOpeningHours(opening, closing, effective);
        return new RestaurantAvailabilityDto(
                restaurant.getId(),
                available,
                effective,
                restaurant.getOpeningHours(),
                restaurant.getClosingHours()
        );
    }

    @Override
    public MenuItemPricingDto getMenuItemPricing(Long menuItemId) {
        if (menuItemId == null) {
            throw new IllegalArgumentException("Menu item id is required");
        }
        return pricingCache.compute(menuItemId, (id, existing) -> {
            MenuItem menuItem = getMenuItemOrThrow(menuItemId);
            return new MenuItemPricingDto(
                    menuItem.getId(),
                    menuItem.getPrice(),
                    menuItem.getPromotionPrice(),
                    menuItem.getPromotionLabel(),
                    menuItem.getPromotionActive(),
                    Instant.now()
            );
        });
    }

    private Map<Long, List<MenuItem>> groupPromotedItems(List<Restaurant> restaurants) {
        if (restaurants == null || restaurants.isEmpty()) {
            return Map.of();
        }
        List<Long> restaurantIds = restaurants.stream()
                .map(Restaurant::getId)
                .filter(Objects::nonNull)
                .toList();
        if (restaurantIds.isEmpty()) {
            return Map.of();
        }
        return menuItemRepository.findByRestaurant_IdInAndPromotionActiveTrue(restaurantIds).stream()
                .filter(item -> Boolean.TRUE.equals(item.getPromotionActive()))
                .filter(item -> item.getRestaurant() != null && item.getRestaurant().getId() != null)
                .collect(Collectors.groupingBy(item -> item.getRestaurant().getId()));
    }

    private RestaurantSearchItemDto toDto(Restaurant restaurant, List<MenuItem> promotedItems) {
        List<MenuItemPromotionDto> promotedMenuItems = promotedItems == null ? List.of() : promotedItems.stream()
                .map(this::toPromotionDto)
                .toList();
        return new RestaurantSearchItemDto(
                restaurant.getId(),
                restaurant.getName(),
                restaurant.getDeliveryTimeRange(),
                restaurant.getRating(),
                Boolean.TRUE.equals(restaurant.getTopChoice()),
                Boolean.TRUE.equals(restaurant.getFreeDelivery()),
                restaurant.getImageUrl(),
                promotedMenuItems
        );
    }

    private MenuItemPromotionDto toPromotionDto(MenuItem menuItem) {
        return new MenuItemPromotionDto(
                menuItem.getId(),
                menuItem.getName(),
                menuItem.getPrice(),
                menuItem.getPromotionPrice(),
                menuItem.getPromotionLabel(),
                firstImage(menuItem)
        );
    }

    private String firstImage(MenuItem item) {
        return (item.getImageUrls() != null && !item.getImageUrls().isEmpty())
                ? item.getImageUrls().get(0)
                : null;
    }

    private Sort toSort(RestaurantSearchSort sort) {
        RestaurantSearchSort effectiveSort = sort == null ? RestaurantSearchSort.PICKED : sort;
        return switch (effectiveSort) {
            case POPULAR -> Sort.by(Sort.Order.desc("topEat"), Sort.Order.desc("rating"), Sort.Order.asc("name"));
            case RATING -> Sort.by(Sort.Order.desc("rating"), Sort.Order.asc("name"));
            case PICKED -> Sort.by(Sort.Order.desc("topChoice"), Sort.Order.desc("rating"), Sort.Order.asc("name"));
        };
    }

    private Specification<Restaurant> buildSpecification(RestaurantSearchQuery query) {
        Specification<Restaurant> specification = Specification.where(null);
        if (query == null) {
            return specification;
        }

        if (query.query() != null && !query.query().isBlank()) {
            String like = "%" + query.query().toLowerCase(Locale.ROOT) + "%";
            specification = specification.and((root, cq, cb) -> cb.or(
                    cb.like(cb.lower(root.get("name")), like),
                    cb.like(cb.lower(root.get("description")), like)
            ));
        }

        if (Boolean.TRUE.equals(query.hasPromotion())) {
            specification = specification.and((root, cq, cb) -> {
                cq.distinct(true);
                return cb.equal(root.join("menuItems").get("promotionActive"), Boolean.TRUE);
            });
        }

        if (Boolean.TRUE.equals(query.isTopChoice())) {
            specification = specification.and((root, cq, cb) -> cb.isTrue(root.get("topChoice")));
        }

        if (Boolean.TRUE.equals(query.hasFreeDelivery())) {
            specification = specification.and((root, cq, cb) -> cb.isTrue(root.get("freeDelivery")));
        }

        if (Boolean.TRUE.equals(query.topEatOnly())) {
            specification = specification.and((root, cq, cb) -> cb.isTrue(root.get("topEat")));
        }

        if (query.maxDeliveryFee() != null) {
            specification = specification.and((root, cq, cb) -> cb.or(
                    cb.isNull(root.get("deliveryFee")),
                    cb.lessThanOrEqualTo(root.get("deliveryFee"), query.maxDeliveryFee())
            ));
        }

        return specification;
    }

    private LocalTime parseTime(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        try {
            return LocalTime.parse(value);
        } catch (Exception ignored) {
            return null;
        }
    }

    private boolean isWithinOpeningHours(LocalTime opening, LocalTime closing, Instant at) {
        if (opening == null || closing == null) {
            return true;
        }
        LocalTime current = at.atZone(ZoneOffset.UTC).toLocalTime();
        if (opening.equals(closing)) {
            return true;
        }
        if (opening.isBefore(closing)) {
            return !current.isBefore(opening) && !current.isAfter(closing);
        }
        // Overnight opening hours
        return !current.isBefore(opening) || !current.isAfter(closing);
    }
}
