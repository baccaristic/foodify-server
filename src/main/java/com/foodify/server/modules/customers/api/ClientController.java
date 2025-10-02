package com.foodify.server.modules.customers.api;

import com.foodify.server.modules.restaurants.dto.RestaurantItemDto;
import com.foodify.server.modules.restaurants.mapper.RestaurantMapper;
import com.foodify.server.modules.orders.domain.Order;
import com.foodify.server.modules.restaurants.domain.Restaurant;
import com.foodify.server.modules.identity.repository.ClientRepository;
import com.foodify.server.modules.restaurants.repository.RestaurantRepository;
import com.foodify.server.modules.customers.application.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/client")
@RequiredArgsConstructor
public class ClientController {
    private final RestaurantRepository restaurantRepository;
    private final ClientService clientService;
    private final ClientRepository clientRepository;
    private final RestaurantMapper restaurantMapper;

    @PreAuthorize("hasAuthority('ROLE_CLIENT')")
    @GetMapping("/nearby")
    public ResponseEntity<?> getNearbyRestaurants(
            @RequestParam double lat,
            @RequestParam double lng,
            @RequestParam(defaultValue = "1000") double radiusKm
    ) {
        List<Restaurant> nearby = restaurantRepository.findNearby(lat, lng, radiusKm);
        return ResponseEntity.ok(restaurantMapper.toDto(nearby));
    }
    @PreAuthorize("hasAuthority('ROLE_CLIENT')")
    @GetMapping("/my-orders")
    public List<Order> getMyOrders(Authentication authentication) {
        Long userId = Long.parseLong((String)authentication.getPrincipal());
        return this.clientService.getMyOrders(clientRepository.findById(userId).orElse(null));
    }

    @PreAuthorize("hasAuthority('ROLE_CLIENT')")
    @GetMapping("/order/{id}")
    public ResponseEntity<Order> getOrder(@PathVariable Long id, Authentication authentication) {
        Order order = this.clientService.getMyOrder(id);
        if (order == null) {
            return ResponseEntity.notFound().build();
        }
        Long userId = Long.parseLong((String)authentication.getPrincipal());
        if (order.getClient().getId().equals(userId)) {
            return ResponseEntity.ok(order);
        }
        return ResponseEntity.badRequest().build();
    }

    @PreAuthorize("hasAuthority('ROLE_CLIENT')")
    @GetMapping("/restaurant/{id}")
    public RestaurantItemDto getRestaurant(@PathVariable Long id) {
        RestaurantItemDto dto = new RestaurantItemDto();
        Restaurant restaurant = this.restaurantRepository.findById(id).orElse(null);
        dto.setId(restaurant.getId());
        dto.setName(restaurant.getName());
        dto.setAddress(restaurant.getAddress());
        dto.setPhone(restaurant.getPhone());
        dto.setDescription(restaurant.getDescription());
        dto.setLatitude(restaurant.getLatitude());
        dto.setLongitude(restaurant.getLongitude());
        dto.setImageUrl(restaurant.getImageUrl());
        dto.setMenu(restaurant.getMenu());
        dto.setRating(restaurant.getRating());
        dto.setType(restaurant.getType());
        dto.setClosingHours(restaurant.getClosingHours());
        dto.setOpeningHours(restaurant.getOpeningHours());
        return dto;
    }
}
