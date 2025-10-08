package com.foodify.catalogservice.service;

import com.foodify.catalogservice.api.CatalogMenuItemPricingDto;
import com.foodify.catalogservice.api.CatalogMenuItemPromotionDto;
import com.foodify.catalogservice.api.CatalogRestaurantAvailabilityDto;
import com.foodify.catalogservice.api.CatalogRestaurantSearchItemDto;
import com.foodify.catalogservice.api.CatalogRestaurantSearchResponseDto;
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
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CatalogApplicationService {

    private final RestaurantRepository restaurantRepository;
    private final MenuItemRepository menuItemRepository;
    private final MenuItemExtraRepository menuItemExtraRepository;
    private final ConcurrentMap<Long, CatalogMenuItemPricingDto> pricingCache = new ConcurrentHashMap<>();

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

    @Transactional(readOnly = true)
    public CatalogRestaurantSearchResponseDto searchRestaurants(String query,
                                                                Boolean hasPromotion,
                                                                Boolean isTopChoice,
                                                                Boolean hasFreeDelivery,
                                                                Boolean topEatOnly,
                                                                Double maxDeliveryFee,
                                                                String sort,
                                                                int page,
                                                                int pageSize) {
        Specification<Restaurant> specification = buildSpecification(query, hasPromotion, isTopChoice,
                hasFreeDelivery, topEatOnly, maxDeliveryFee);
        Sort order = toSort(sort);
        Pageable pageable = PageRequest.of(Math.max(page - 1, 0), Math.max(pageSize, 1), order);

        Page<Restaurant> restaurants = restaurantRepository.findAll(specification, pageable);
        List<Restaurant> content = restaurants.getContent();
        List<Long> restaurantIds = content.stream()
                .map(Restaurant::getId)
                .filter(Objects::nonNull)
                .toList();
        var promotionsByRestaurant = restaurantIds.isEmpty() ? java.util.Map.<Long, List<MenuItem>>of()
                : menuItemRepository.findByRestaurant_IdInAndPromotionActiveTrue(restaurantIds).stream()
                .filter(item -> Boolean.TRUE.equals(item.getPromotionActive()))
                .filter(item -> item.getRestaurant() != null && item.getRestaurant().getId() != null)
                .collect(Collectors.groupingBy(item -> item.getRestaurant().getId()));

        List<CatalogRestaurantSearchItemDto> items = content.stream()
                .map(restaurant -> new CatalogRestaurantSearchItemDto(
                        restaurant.getId(),
                        restaurant.getName(),
                        restaurant.getDeliveryTimeRange(),
                        restaurant.getRating(),
                        Boolean.TRUE.equals(restaurant.getTopChoice()),
                        Boolean.TRUE.equals(restaurant.getFreeDelivery()),
                        restaurant.getImageUrl(),
                        promotionsByRestaurant.getOrDefault(restaurant.getId(), List.of()).stream()
                                .map(this::toPromotionDto)
                                .toList()
                ))
                .toList();

        return new CatalogRestaurantSearchResponseDto(items, page, pageSize, restaurants.getTotalElements());
    }

    @Transactional(readOnly = true)
    public CatalogRestaurantAvailabilityDto getAvailability(Long restaurantId, Instant asOf) {
        Restaurant restaurant = getRestaurantOrThrow(restaurantId);
        Instant effective = Optional.ofNullable(asOf).orElse(Instant.now());
        LocalTime opening = parseTime(restaurant.getOpeningHours());
        LocalTime closing = parseTime(restaurant.getClosingHours());
        boolean available = isWithinOpeningHours(opening, closing, effective);
        return new CatalogRestaurantAvailabilityDto(
                restaurant.getId(),
                available,
                effective,
                restaurant.getOpeningHours(),
                restaurant.getClosingHours()
        );
    }

    @Transactional(readOnly = true)
    public CatalogMenuItemPricingDto getMenuItemPricing(Long menuItemId) {
        if (menuItemId == null) {
            throw new IllegalArgumentException("Menu item id is required");
        }
        return pricingCache.compute(menuItemId, (id, existing) -> {
            MenuItem menuItem = getMenuItemOrThrow(menuItemId);
            return new CatalogMenuItemPricingDto(
                    menuItem.getId(),
                    menuItem.getPrice(),
                    menuItem.getPromotionPrice(),
                    menuItem.getPromotionLabel(),
                    menuItem.getPromotionActive(),
                    Instant.now()
            );
        });
    }

    private CatalogMenuItemPromotionDto toPromotionDto(MenuItem menuItem) {
        return new CatalogMenuItemPromotionDto(
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

    private Specification<Restaurant> buildSpecification(String query,
                                                         Boolean hasPromotion,
                                                         Boolean isTopChoice,
                                                         Boolean hasFreeDelivery,
                                                         Boolean topEatOnly,
                                                         Double maxDeliveryFee) {
        Specification<Restaurant> specification = Specification.where(null);

        if (query != null && !query.isBlank()) {
            String like = "%" + query.toLowerCase(java.util.Locale.ROOT) + "%";
            specification = specification.and((root, cq, cb) -> cb.or(
                    cb.like(cb.lower(root.get("name")), like),
                    cb.like(cb.lower(root.get("description")), like)
            ));
        }

        if (Boolean.TRUE.equals(hasPromotion)) {
            specification = specification.and((root, cq, cb) -> {
                cq.distinct(true);
                return cb.equal(root.join("menuItems").get("promotionActive"), Boolean.TRUE);
            });
        }

        if (Boolean.TRUE.equals(isTopChoice)) {
            specification = specification.and((root, cq, cb) -> cb.isTrue(root.get("topChoice")));
        }

        if (Boolean.TRUE.equals(hasFreeDelivery)) {
            specification = specification.and((root, cq, cb) -> cb.isTrue(root.get("freeDelivery")));
        }

        if (Boolean.TRUE.equals(topEatOnly)) {
            specification = specification.and((root, cq, cb) -> cb.isTrue(root.get("topEat")));
        }

        if (maxDeliveryFee != null) {
            specification = specification.and((root, cq, cb) -> cb.or(
                    cb.isNull(root.get("deliveryFee")),
                    cb.lessThanOrEqualTo(root.get("deliveryFee"), maxDeliveryFee)
            ));
        }

        return specification;
    }

    private Sort toSort(String sort) {
        if (sort == null || sort.isBlank()) {
            return Sort.by(Sort.Order.desc("topChoice"), Sort.Order.desc("rating"), Sort.Order.asc("name"));
        }
        return switch (sort.toUpperCase(java.util.Locale.ROOT)) {
            case "POPULAR" -> Sort.by(Sort.Order.desc("topEat"), Sort.Order.desc("rating"), Sort.Order.asc("name"));
            case "RATING" -> Sort.by(Sort.Order.desc("rating"), Sort.Order.asc("name"));
            default -> Sort.by(Sort.Order.desc("topChoice"), Sort.Order.desc("rating"), Sort.Order.asc("name"));
        };
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
        return !current.isBefore(opening) || !current.isAfter(closing);
    }
}
