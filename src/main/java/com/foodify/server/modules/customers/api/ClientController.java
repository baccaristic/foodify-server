package com.foodify.server.modules.customers.api;

import com.foodify.server.modules.customers.application.ClientService;
import com.foodify.server.modules.customers.dto.ClientFavoriteIds;
import com.foodify.server.modules.customers.dto.ClientFavoritesResponse;
import com.foodify.server.modules.identity.repository.ClientRepository;
import com.foodify.server.modules.orders.domain.Order;
import com.foodify.server.modules.orders.dto.OrderDto;
import com.foodify.server.modules.restaurants.application.DeliveryFeeCalculator;
import com.foodify.server.modules.restaurants.application.RestaurantDetailsService;
import com.foodify.server.modules.restaurants.domain.MenuItem;
import com.foodify.server.modules.restaurants.domain.Restaurant;
import com.foodify.server.modules.restaurants.dto.RestaurantDetailsResponse;
import com.foodify.server.modules.restaurants.dto.RestaurantDisplayDto;
import com.foodify.server.modules.restaurants.dto.PageResponse;
import com.foodify.server.modules.restaurants.dto.NearbyRestaurantsResponse;
import com.foodify.server.modules.restaurants.dto.PaginatedRestaurantSection;
import com.foodify.server.modules.restaurants.dto.RestaurantSection;
import com.foodify.server.modules.restaurants.mapper.RestaurantMapper;
import com.foodify.server.modules.restaurants.repository.MenuItemRepository;
import com.foodify.server.modules.restaurants.repository.RestaurantRepository;
import com.foodify.server.modules.orders.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import jakarta.persistence.EntityNotFoundException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/client")
@RequiredArgsConstructor
public class ClientController {
    private final RestaurantRepository restaurantRepository;
    private final ClientService clientService;
    private final ClientRepository clientRepository;
    private final RestaurantMapper restaurantMapper;
    private final RestaurantDetailsService restaurantDetailsService;
    private final DeliveryFeeCalculator deliveryFeeCalculator;
    private final OrderRepository orderRepository;
    private final MenuItemRepository menuItemRepository;

    private Long extractUserId(Authentication authentication) {
        return Long.parseLong((String) authentication.getPrincipal());
    }

    @PreAuthorize("hasAuthority('ROLE_CLIENT')")
    @GetMapping("/nearby")
    public ResponseEntity<NearbyRestaurantsResponse> getNearbyRestaurants(
            @RequestParam double lat,
            @RequestParam double lng,
            @RequestParam(defaultValue = "1000") double radiusKm,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer pageSize,
            Authentication authentication
    ) {
        Long userId = extractUserId(authentication);
        ClientFavoriteIds favoriteIds = clientService.getFavoriteIds(userId);
        Set<Long> favoriteRestaurantIds = favoriteIds.restaurantIds();
        int effectivePage = page != null && page >= 0 ? page : 0;
        int effectivePageSize = pageSize != null && pageSize > 0 ? pageSize : 20;
        PageRequest pageRequest = PageRequest.of(effectivePage, effectivePageSize);
        List<Restaurant> topPicksEntities = restaurantRepository
                .findTopChoiceNearby(lat, lng, radiusKm, PageRequest.of(0, 5))
                .getContent();
        List<Restaurant> promotionEntities = restaurantRepository
                .findNearbyWithPromotions(lat, lng, radiusKm, PageRequest.of(0, 5))
                .getContent();

        Set<Long> excludedFromOthers = new HashSet<>();
        List<RestaurantDisplayDto> topPicks = mapAndEnrich(topPicksEntities, favoriteRestaurantIds, lat, lng);
        topPicks.forEach(restaurant -> {
            if (restaurant.getId() != null) {
                excludedFromOthers.add(restaurant.getId());
            }
        });

        List<RestaurantDisplayDto> promotions = mapAndEnrich(promotionEntities, favoriteRestaurantIds, lat, lng);
        promotions.forEach(restaurant -> {
            if (restaurant.getId() != null) {
                excludedFromOthers.add(restaurant.getId());
            }
        });

        List<RestaurantDisplayDto> orderAgain = getOrderAgainRestaurants(userId, lat, lng, radiusKm, favoriteRestaurantIds);
        orderAgain.forEach(restaurant -> {
            if (restaurant.getId() != null) {
                excludedFromOthers.add(restaurant.getId());
            }
        });

        Page<Restaurant> othersPage = excludedFromOthers.isEmpty()
                ? restaurantRepository.findNearby(lat, lng, radiusKm, pageRequest)
                : restaurantRepository.findNearbyExcluding(lat, lng, radiusKm, excludedFromOthers, pageRequest);

        List<RestaurantDisplayDto> others = mapAndEnrich(othersPage.getContent(), favoriteRestaurantIds, lat, lng);

        NearbyRestaurantsResponse response = new NearbyRestaurantsResponse(
                new RestaurantSection("carousel", topPicks),
                new RestaurantSection("carousel", orderAgain),
                new RestaurantSection("flatList", promotions),
                new PaginatedRestaurantSection(
                        "flatList",
                        others,
                        othersPage.getNumber(),
                        othersPage.getSize(),
                        othersPage.getTotalElements()
                )
        );

        return ResponseEntity.ok(response);
    }

    private List<RestaurantDisplayDto> mapAndEnrich(List<Restaurant> restaurants,
                                                    Set<Long> favoriteRestaurantIds,
                                                    double clientLat,
                                                    double clientLng) {
        if (restaurants.isEmpty()) {
            return Collections.emptyList();
        }
        List<RestaurantDisplayDto> dtos = restaurantMapper.toDto(restaurants);
        Map<Long, List<MenuItem>> promotionsByRestaurant = groupPromotions(restaurants);
        for (int i = 0; i < restaurants.size(); i++) {
            Restaurant entity = restaurants.get(i);
            RestaurantDisplayDto dto = dtos.get(i);
            if (entity.getId() != null && favoriteRestaurantIds.contains(entity.getId())) {
                dto.setFavorite(true);
            }
            deliveryFeeCalculator.calculateFee(clientLat, clientLng, entity.getLatitude(), entity.getLongitude())
                    .ifPresent(dto::setDeliveryFee);
            applyPromotionInfo(dto, entity, promotionsByRestaurant);
        }
        return dtos;
    }

    private Map<Long, List<MenuItem>> groupPromotions(List<Restaurant> restaurants) {
        if (restaurants == null || restaurants.isEmpty()) {
            return Map.of();
        }
        List<Long> ids = restaurants.stream()
                .map(Restaurant::getId)
                .filter(Objects::nonNull)
                .toList();
        if (ids.isEmpty()) {
            return Map.of();
        }
        return menuItemRepository.findByRestaurant_IdInAndPromotionActiveTrue(ids).stream()
                .filter(item -> item.getRestaurant() != null && item.getRestaurant().getId() != null)
                .collect(Collectors.groupingBy(item -> item.getRestaurant().getId()));
    }

    private void applyPromotionInfo(RestaurantDisplayDto dto,
                                    Restaurant entity,
                                    Map<Long, List<MenuItem>> promotionsByRestaurant) {
        Long restaurantId = entity.getId();
        if (restaurantId == null) {
            dto.setHasPromotion(false);
            dto.setPromotionSummary(null);
            return;
        }
        List<MenuItem> promotedItems = promotionsByRestaurant.getOrDefault(restaurantId, List.of());
        List<Double> discounts = promotedItems.stream()
                .map(this::calculateDiscountPercentage)
                .flatMap(Optional::stream)
                .toList();
        if (discounts.isEmpty()) {
            dto.setHasPromotion(false);
            dto.setPromotionSummary(null);
            return;
        }
        dto.setHasPromotion(true);
        dto.setPromotionSummary(buildPromotionSummary(discounts));
    }

    private Optional<Double> calculateDiscountPercentage(MenuItem menuItem) {
        if (!Boolean.TRUE.equals(menuItem.getPromotionActive())) {
            return Optional.empty();
        }
        double price = menuItem.getPrice();
        Double promotionPrice = menuItem.getPromotionPrice();
        if (promotionPrice == null || promotionPrice <= 0 || price <= 0 || promotionPrice >= price) {
            return Optional.empty();
        }
        double discount = (price - promotionPrice) / price * 100.0;
        if (discount <= 0) {
            return Optional.empty();
        }
        return Optional.of(discount);
    }

    private String buildPromotionSummary(List<Double> discounts) {
        if (discounts.size() == 1) {
            return formatPercent(discounts.get(0));
        }
        double min = discounts.stream().mapToDouble(Double::doubleValue).min().orElse(0);
        double max = discounts.stream().mapToDouble(Double::doubleValue).max().orElse(0);
        if (Double.compare(min, max) == 0) {
            return formatPercent(max);
        }
        return "from %s to %s".formatted(formatPercent(min), formatPercent(max));
    }

    private String formatPercent(double discount) {
        BigDecimal value = BigDecimal.valueOf(discount).setScale(0, RoundingMode.HALF_UP);
        return "-%s%%".formatted(value.stripTrailingZeros().toPlainString());
    }

    private List<RestaurantDisplayDto> getOrderAgainRestaurants(Long userId,
                                                                double lat,
                                                                double lng,
                                                                double radiusKm,
                                                                Set<Long> favoriteRestaurantIds) {
        return clientRepository.findById(userId)
                .map(client -> {
                    Page<Order> recentOrders = orderRepository.findAllByClient(
                            client,
                            PageRequest.of(0, 20, Sort.by("date").descending())
                    );

                    List<Restaurant> restaurants = new ArrayList<>();
                    Set<Long> seen = new LinkedHashSet<>();

                    for (Order order : recentOrders) {
                        Restaurant restaurant = order.getRestaurant();
                        if (restaurant == null) {
                            continue;
                        }
                        Long restaurantId = restaurant.getId();
                        if (restaurantId == null || !isWithinRadius(restaurant, lat, lng, radiusKm) || !seen.add(restaurantId)) {
                            continue;
                        }
                        restaurants.add(restaurant);
                        if (restaurants.size() == 5) {
                            break;
                        }
                    }

                    return mapAndEnrich(restaurants, favoriteRestaurantIds, lat, lng);
                })
                .orElseGet(Collections::emptyList);
    }

    private boolean isWithinRadius(Restaurant restaurant, double clientLat, double clientLng, double radiusKm) {
        double distance = haversine(clientLat, clientLng, restaurant.getLatitude(), restaurant.getLongitude());
        return distance <= radiusKm;
    }

    private double haversine(double lat1, double lon1, double lat2, double lon2) {
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return 6371.0 * c;
    }

    @PreAuthorize("hasAuthority('ROLE_CLIENT')")
    @GetMapping("/my-orders")
    public PageResponse<OrderDto> getMyOrders(
            Authentication authentication,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int pageSize
    ) {
        Long userId = extractUserId(authentication);

        int effectivePage = Math.max(page, 0);
        int effectivePageSize = pageSize > 0 ? Math.min(pageSize, 50) : 20;

        PageRequest pageRequest = PageRequest.of(effectivePage, effectivePageSize, Sort.by("date").descending());
        Page<OrderDto> orders = this.clientService.getMyOrders(clientRepository.findById(userId).orElse(null), pageRequest);

        return new PageResponse<>(
                orders.getContent(),
                orders.getNumber(),
                orders.getSize(),
                orders.getTotalElements()
        );
    }

    @PreAuthorize("hasAuthority('ROLE_CLIENT')")
    @GetMapping("/order/{id}")
    public ResponseEntity<Order> getOrder(@PathVariable Long id, Authentication authentication) {
        Order order = this.clientService.getMyOrder(id);
        if (order == null) {
            return ResponseEntity.notFound().build();
        }
        Long userId = extractUserId(authentication);
        if (order.getClient().getId().equals(userId)) {
            return ResponseEntity.ok(order);
        }
        return ResponseEntity.badRequest().build();
    }

    @PreAuthorize("hasAuthority('ROLE_CLIENT')")
    @GetMapping("/restaurant/{id}")
    public RestaurantDetailsResponse getRestaurant(
            @PathVariable Long id,
            @RequestParam(required = false) Double lat,
            @RequestParam(required = false) Double lng,
            Authentication authentication
    ) {
        Long userId = extractUserId(authentication);
        ClientFavoriteIds favoriteIds = clientService.getFavoriteIds(userId);
        return restaurantDetailsService.getRestaurantDetails(
                id,
                lat,
                lng,
                favoriteIds.restaurantIds(),
                favoriteIds.menuItemIds()
        );
    }

    @PreAuthorize("hasAuthority('ROLE_CLIENT')")
    @PostMapping("/favorites/restaurants/{restaurantId}")
    public ResponseEntity<Void> addFavoriteRestaurant(@PathVariable Long restaurantId, Authentication authentication) {
        Long userId = extractUserId(authentication);
        try {
            clientService.addFavoriteRestaurant(userId, restaurantId);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @PreAuthorize("hasAuthority('ROLE_CLIENT')")
    @DeleteMapping("/favorites/restaurants/{restaurantId}")
    public ResponseEntity<Void> removeFavoriteRestaurant(@PathVariable Long restaurantId, Authentication authentication) {
        Long userId = extractUserId(authentication);
        try {
            clientService.removeFavoriteRestaurant(userId, restaurantId);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @PreAuthorize("hasAuthority('ROLE_CLIENT')")
    @PostMapping("/favorites/menu-items/{menuItemId}")
    public ResponseEntity<Void> addFavoriteMenuItem(@PathVariable Long menuItemId, Authentication authentication) {
        Long userId = extractUserId(authentication);
        try {
            clientService.addFavoriteMenuItem(userId, menuItemId);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @PreAuthorize("hasAuthority('ROLE_CLIENT')")
    @DeleteMapping("/favorites/menu-items/{menuItemId}")
    public ResponseEntity<Void> removeFavoriteMenuItem(@PathVariable Long menuItemId, Authentication authentication) {
        Long userId = extractUserId(authentication);
        try {
            clientService.removeFavoriteMenuItem(userId, menuItemId);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @PreAuthorize("hasAuthority('ROLE_CLIENT')")
    @GetMapping("/favorites")
    public ResponseEntity<ClientFavoritesResponse> getFavorites(Authentication authentication) {
        Long userId = extractUserId(authentication);
        try {
            return ResponseEntity.ok(clientService.getFavorites(userId));
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }
}
