package com.foodify.server.modules.customers.api;

import com.foodify.server.modules.customers.application.ClientService;
import com.foodify.server.modules.identity.repository.ClientRepository;
import com.foodify.server.modules.orders.domain.Order;
import com.foodify.server.modules.restaurants.application.RestaurantDetailsService;
import com.foodify.server.modules.restaurants.dto.RestaurantDetailsResponse;
import com.foodify.server.modules.restaurants.mapper.RestaurantMapper;
import com.foodify.server.modules.restaurants.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/client")
@RequiredArgsConstructor
public class ClientController {
    private final RestaurantRepository restaurantRepository;
    private final ClientService clientService;
    private final ClientRepository clientRepository;
    private final RestaurantMapper restaurantMapper;
    private final RestaurantDetailsService restaurantDetailsService;

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
    public RestaurantDetailsResponse getRestaurant(@PathVariable Long id) {
        return restaurantDetailsService.getRestaurantDetails(id);
    }
}
