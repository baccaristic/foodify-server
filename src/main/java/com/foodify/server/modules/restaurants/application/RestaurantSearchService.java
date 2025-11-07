package com.foodify.server.modules.restaurants.application;

import com.foodify.server.modules.restaurants.domain.MenuItem;
import com.foodify.server.modules.restaurants.domain.Restaurant;
import com.foodify.server.modules.restaurants.domain.RestaurantCategory;
import com.foodify.server.modules.restaurants.domain.RestaurantSpecialDay;
import com.foodify.server.modules.restaurants.domain.RestaurantWeeklyOperatingHour;
import com.foodify.server.modules.restaurants.dto.PageResponse;
import com.foodify.server.modules.restaurants.dto.MenuItemPromotionDto;
import com.foodify.server.modules.restaurants.dto.RestaurantSearchItemDto;
import com.foodify.server.modules.restaurants.dto.RestaurantSearchQuery;
import com.foodify.server.modules.restaurants.dto.RestaurantSearchSort;
import com.foodify.server.modules.restaurants.repository.MenuItemRepository;
import com.foodify.server.modules.restaurants.repository.RestaurantRepository;
import com.foodify.server.modules.restaurants.repository.RestaurantOperatingHourRepository;
import com.foodify.server.modules.restaurants.repository.RestaurantSpecialDayRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import jakarta.persistence.criteria.JoinType;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RestaurantSearchService {

    private static final double MAX_SEARCH_RADIUS_KM = 10.0;

    private final RestaurantRepository restaurantRepository;
    private final MenuItemRepository menuItemRepository;
    private final DeliveryFeeCalculator deliveryFeeCalculator;
    private final RestaurantDeliveryMetricsService deliveryMetricsService;
    private final RestaurantOperatingStatusService operatingStatusService;
    private final RestaurantOperatingHourRepository operatingHourRepository;
    private final RestaurantSpecialDayRepository specialDayRepository;

    public PageResponse<RestaurantSearchItemDto> search(RestaurantSearchQuery query, Set<Long> favoriteRestaurantIds, Set<Long> favoriteMenuItemIds) {
        Specification<Restaurant> specification = buildSpecification(query);
        Sort sort = toSort(query.sort());
        int page = query.page() != null && query.page() > 0 ? query.page() : 1;
        int pageSize = query.pageSize() != null && query.pageSize() > 0 ? query.pageSize() : 20;
        Pageable pageable = PageRequest.of(Math.max(page - 1, 0), pageSize, sort);

        Page<Restaurant> restaurants = restaurantRepository.findAll(specification, pageable);
        List<Restaurant> restaurantContent = restaurants.getContent();
        Double clientLatitude = query.clientLatitude();
        Double clientLongitude = query.clientLongitude();
        List<Restaurant> radiusFiltered = filterByRadius(restaurantContent, clientLatitude, clientLongitude);
        Map<Long, List<MenuItem>> promotionsByRestaurant = groupPromotedItems(radiusFiltered);
        Set<Long> restaurantFavorites = favoriteRestaurantIds == null ? Set.of() : favoriteRestaurantIds;
        Set<Long> menuFavorites = favoriteMenuItemIds == null ? Set.of() : favoriteMenuItemIds;

        List<RestaurantSearchItemDto> items = radiusFiltered.stream()
                .map(restaurant -> toDto(
                        restaurant,
                        promotionsByRestaurant.getOrDefault(restaurant.getId(), List.of()),
                        restaurantFavorites,
                        menuFavorites,
                        clientLatitude,
                        clientLongitude
                ))
                .toList();

        if (query.maxDeliveryFee() != null) {
            items = items.stream()
                    .filter(item -> item.deliveryFee() == null || item.deliveryFee() <= query.maxDeliveryFee())
                    .toList();
        }

        long totalElements = restaurants.getTotalElements();
        if (clientLatitude != null && clientLongitude != null) {
            totalElements = radiusFiltered.size();
        }
        if (query.maxDeliveryFee() != null) {
            totalElements = items.size();
        }

        return new PageResponse<>(items, page, pageSize, totalElements);
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
        return menuItemRepository.findByRestaurant_IdInAndPromotionActiveTrueAndAvailableTrue(restaurantIds).stream()
                .filter(item -> Boolean.TRUE.equals(item.getPromotionActive()))
                .filter(item -> item.getRestaurant() != null && item.getRestaurant().getId() != null)
                .collect(Collectors.groupingBy(item -> item.getRestaurant().getId()));
    }

    private RestaurantSearchItemDto toDto(
            Restaurant restaurant,
            List<MenuItem> promotedItems,
            Set<Long> favoriteRestaurantIds,
            Set<Long> favoriteMenuItemIds,
            Double clientLatitude,
            Double clientLongitude
    ) {
        List<MenuItemPromotionDto> promotedMenuItems = promotedItems == null ? List.of() : promotedItems.stream()
                .map(item -> toPromotionDto(item, favoriteMenuItemIds))
                .toList();
        Double deliveryFee = deliveryFeeCalculator.calculateFee(
                clientLatitude,
                clientLongitude,
                restaurant.getLatitude(),
                restaurant.getLongitude()
        ).orElse(null);
        
        // Calculate estimated delivery time
        Integer estimatedDeliveryTime = null;
        if (clientLatitude != null && clientLongitude != null && restaurant.getId() != null) {
            double distance = deliveryFeeCalculator.calculateDistance(
                    clientLatitude,
                    clientLongitude,
                    restaurant.getLatitude(),
                    restaurant.getLongitude()
            ).orElse(0.0);
            estimatedDeliveryTime = deliveryMetricsService.calculateEstimatedDeliveryTime(
                    restaurant.getId(),
                    distance
            );
        }
        
        // Calculate isOpen status and get operating hours from weekly schedule
        LocalDate currentDate = LocalDate.now();
        LocalTime currentTime = LocalTime.now();
        List<RestaurantWeeklyOperatingHour> weeklyHours = restaurant.getId() != null
                ? operatingHourRepository.findByRestaurant_IdOrderByDayOfWeekAsc(restaurant.getId())
                : List.of();
        List<RestaurantSpecialDay> specialDays = restaurant.getId() != null
                ? specialDayRepository.findByRestaurant_IdOrderByDateAsc(restaurant.getId())
                : List.of();
        
        boolean isOpen = operatingStatusService.isRestaurantOpen(weeklyHours, specialDays, currentDate, currentTime);
        RestaurantOperatingStatusService.OperatingHours operatingHours = 
                operatingStatusService.getOperatingHoursForDate(weeklyHours, specialDays, currentDate);
        
        String openingHours = operatingHours != null ? operatingHours.getOpeningHoursFormatted() : restaurant.getOpeningHours();
        String closingHours = operatingHours != null ? operatingHours.getClosingHoursFormatted() : restaurant.getClosingHours();
        
        Set<RestaurantCategory> categories = restaurant.getCategories() == null || restaurant.getCategories().isEmpty()
                ? Set.of()
                : Set.copyOf(restaurant.getCategories());
        return new RestaurantSearchItemDto(
                restaurant.getId(),
                restaurant.getName(),
                restaurant.getNameEn(),
                restaurant.getNameFr(),
                restaurant.getNameAr(),
                restaurant.getDeliveryTimeRange(),
                restaurant.getRating(),
                Boolean.TRUE.equals(restaurant.getTopChoice()),
                Boolean.TRUE.equals(restaurant.getFreeDelivery()),
                deliveryFee,
                favoriteRestaurantIds.contains(restaurant.getId()),
                restaurant.getImageUrl(),
                restaurant.getIconUrl(),
                categories,
                promotedMenuItems,
                estimatedDeliveryTime,
                isOpen,
                openingHours,
                closingHours
        );
    }

    private MenuItemPromotionDto toPromotionDto(MenuItem menuItem, Set<Long> favoriteMenuItemIds) {
        return new MenuItemPromotionDto(
                menuItem.getId(),
                menuItem.getName(),
                menuItem.getNameEn(),
                menuItem.getNameFr(),
                menuItem.getNameAr(),
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

        if (query.categories() != null && !query.categories().isEmpty()) {
            specification = specification.and((root, cq, cb) -> {
                cq.distinct(true);
                var categoriesJoin = root.join("categories", JoinType.INNER);
                return categoriesJoin.in(query.categories());
            });
        }

        if (query.clientLatitude() != null && query.clientLongitude() != null) {
            GeoBounds bounds = computeGeoBounds(query.clientLatitude(), query.clientLongitude(), MAX_SEARCH_RADIUS_KM);
            specification = specification.and((root, cq, cb) -> cb.and(
                    cb.between(root.get("latitude"), bounds.minLat(), bounds.maxLat()),
                    cb.between(root.get("longitude"), bounds.minLng(), bounds.maxLng())
            ));
        }

        return specification;
    }

    private List<Restaurant> filterByRadius(List<Restaurant> restaurants,
                                            Double clientLatitude,
                                            Double clientLongitude) {
        if (restaurants == null || restaurants.isEmpty()) {
            return List.of();
        }
        if (clientLatitude == null || clientLongitude == null) {
            return restaurants;
        }
        return restaurants.stream()
                .filter(restaurant -> isWithinRadius(restaurant, clientLatitude, clientLongitude))
                .toList();
    }

    private boolean isWithinRadius(Restaurant restaurant, Double clientLatitude, Double clientLongitude) {
        if (restaurant == null) {
            return false;
        }
        return deliveryFeeCalculator.calculateDistance(
                        clientLatitude,
                        clientLongitude,
                        restaurant.getLatitude(),
                        restaurant.getLongitude()
                )
                .map(distance -> distance <= MAX_SEARCH_RADIUS_KM)
                .orElse(false);
    }

    private GeoBounds computeGeoBounds(double lat, double lng, double radiusKm) {
        double latDelta = radiusKm / 111.0;
        double minLat = clampLatitude(lat - latDelta);
        double maxLat = clampLatitude(lat + latDelta);

        double cosLat = Math.cos(Math.toRadians(lat));
        double safeCosLat = Math.max(Math.abs(cosLat), 1e-6);
        double lonDelta = radiusKm / (111.0 * safeCosLat);
        double minLng = clampLongitude(lng - lonDelta);
        double maxLng = clampLongitude(lng + lonDelta);

        return new GeoBounds(minLat, maxLat, minLng, maxLng);
    }

    private double clampLatitude(double value) {
        return Math.max(-90.0, Math.min(90.0, value));
    }

    private double clampLongitude(double value) {
        if (value > 180.0) {
            return 180.0;
        }
        if (value < -180.0) {
            return -180.0;
        }
        return value;
    }

    private record GeoBounds(double minLat, double maxLat, double minLng, double maxLng) {
    }
}
