package com.foodify.server.modules.restaurants.application;

import com.foodify.server.modules.restaurants.domain.MenuItem;
import com.foodify.server.modules.restaurants.domain.Restaurant;
import com.foodify.server.modules.restaurants.dto.PageResponse;
import com.foodify.server.modules.restaurants.dto.MenuItemPromotionDto;
import com.foodify.server.modules.restaurants.dto.RestaurantSearchItemDto;
import com.foodify.server.modules.restaurants.dto.RestaurantSearchQuery;
import com.foodify.server.modules.restaurants.dto.RestaurantSearchSort;
import com.foodify.server.modules.restaurants.repository.MenuItemRepository;
import com.foodify.server.modules.restaurants.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import jakarta.persistence.criteria.JoinType;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RestaurantSearchService {

    private final RestaurantRepository restaurantRepository;
    private final MenuItemRepository menuItemRepository;

    public PageResponse<RestaurantSearchItemDto> search(RestaurantSearchQuery query, Set<Long> favoriteRestaurantIds, Set<Long> favoriteMenuItemIds) {
        Specification<Restaurant> specification = buildSpecification(query);
        Sort sort = toSort(query.sort());
        int page = query.page() != null && query.page() > 0 ? query.page() : 1;
        int pageSize = query.pageSize() != null && query.pageSize() > 0 ? query.pageSize() : 20;
        Pageable pageable = PageRequest.of(Math.max(page - 1, 0), pageSize, sort);

        Page<Restaurant> restaurants = restaurantRepository.findAll(specification, pageable);
        List<Restaurant> restaurantContent = restaurants.getContent();
        Map<Long, List<MenuItem>> promotionsByRestaurant = groupPromotedItems(restaurantContent);
        Set<Long> restaurantFavorites = favoriteRestaurantIds == null ? Set.of() : favoriteRestaurantIds;
        Set<Long> menuFavorites = favoriteMenuItemIds == null ? Set.of() : favoriteMenuItemIds;
        List<RestaurantSearchItemDto> items = restaurantContent.stream()
                .map(restaurant -> toDto(
                        restaurant,
                        promotionsByRestaurant.getOrDefault(restaurant.getId(), List.of()),
                        restaurantFavorites,
                        menuFavorites
                ))
                .toList();

        return new PageResponse<>(items, page, pageSize, restaurants.getTotalElements());
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

    private RestaurantSearchItemDto toDto(Restaurant restaurant, List<MenuItem> promotedItems, Set<Long> favoriteRestaurantIds, Set<Long> favoriteMenuItemIds) {
        List<MenuItemPromotionDto> promotedMenuItems = promotedItems == null ? List.of() : promotedItems.stream()
                .map(item -> toPromotionDto(item, favoriteMenuItemIds))
                .toList();
        return new RestaurantSearchItemDto(
                restaurant.getId(),
                restaurant.getName(),
                restaurant.getDeliveryTimeRange(),
                restaurant.getRating(),
                Boolean.TRUE.equals(restaurant.getTopChoice()),
                Boolean.TRUE.equals(restaurant.getFreeDelivery()),
                favoriteRestaurantIds.contains(restaurant.getId()),
                restaurant.getImageUrl(),
                promotedMenuItems
        );
    }

    private MenuItemPromotionDto toPromotionDto(MenuItem menuItem, Set<Long> favoriteMenuItemIds) {
        return new MenuItemPromotionDto(
                menuItem.getId(),
                menuItem.getName(),
                menuItem.getPrice(),
                menuItem.getPromotionPrice(),
                menuItem.getPromotionLabel(),
                firstImage(menuItem),
                favoriteMenuItemIds.contains(menuItem.getId())
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
                return cb.equal(root.join("menu", JoinType.INNER).get("promotionActive"), Boolean.TRUE);
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
}
