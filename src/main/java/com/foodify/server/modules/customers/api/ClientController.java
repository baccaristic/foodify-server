package com.foodify.server.modules.customers.api;

import com.foodify.server.modules.customers.application.ClientService;
import com.foodify.server.modules.customers.dto.ClientFavoriteIds;
import com.foodify.server.modules.customers.dto.ClientFavoritesResponse;
import com.foodify.server.modules.identity.repository.ClientRepository;
import com.foodify.server.modules.orders.domain.Order;
import com.foodify.server.modules.orders.dto.OrderDto;
import com.foodify.server.modules.restaurants.application.DeliveryFeeCalculator;
import com.foodify.server.modules.restaurants.application.RestaurantDetailsService;
import com.foodify.server.modules.restaurants.domain.Restaurant;
import com.foodify.server.modules.restaurants.dto.PageResponse;
import com.foodify.server.modules.restaurants.dto.RestaurantDetailsResponse;
import com.foodify.server.modules.restaurants.dto.RestaurantDisplayDto;
import com.foodify.server.modules.restaurants.mapper.RestaurantMapper;
import com.foodify.server.modules.restaurants.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import jakarta.persistence.EntityNotFoundException;

import java.util.List;
import java.util.Set;

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

    private Long extractUserId(Authentication authentication) {
        return Long.parseLong((String) authentication.getPrincipal());
    }

    @PreAuthorize("hasAuthority('ROLE_CLIENT')")
    @GetMapping("/nearby")
    public ResponseEntity<PageResponse<RestaurantDisplayDto>> getNearbyRestaurants(
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
        int effectivePage = page != null && page > 0 ? page : 1;
        int effectivePageSize = pageSize != null && pageSize > 0 ? pageSize : 20;
        PageRequest pageRequest = PageRequest.of(effectivePage - 1, effectivePageSize);
        Page<Restaurant> nearby = restaurantRepository.findNearby(lat, lng, radiusKm, pageRequest);
        List<RestaurantDisplayDto> restaurants = restaurantMapper.toDto(nearby.getContent());
        restaurants.forEach(restaurant -> {
            restaurant.setFavorite(favoriteRestaurantIds.contains(restaurant.getId()));
            deliveryFeeCalculator.calculateFee(lat, lng, restaurant.getLatitude(), restaurant.getLongitude())
                    .ifPresent(restaurant::setDeliveryFee);
        });
        PageResponse<RestaurantDisplayDto> response = new PageResponse<>(
                restaurants,
                effectivePage,
                effectivePageSize,
                nearby.getTotalElements()
        );
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAuthority('ROLE_CLIENT')")
    @GetMapping("/my-orders")
    public List<OrderDto> getMyOrders(Authentication authentication) {
        Long userId = extractUserId(authentication);
        return this.clientService.getMyOrders(clientRepository.findById(userId).orElse(null));
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
