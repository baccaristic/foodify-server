package com.foodify.server.modules.restaurants.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.foodify.server.modules.restaurants.dto.MenuItemRequestDTO;
import com.foodify.server.modules.orders.dto.OrderDto;
import com.foodify.server.modules.restaurants.domain.MenuItem;
import com.foodify.server.modules.identity.domain.RestaurantAdmin;
import com.foodify.server.modules.identity.repository.RestaurantAdminRepository;
import com.foodify.server.modules.restaurants.application.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/restaurant")
@RequiredArgsConstructor
public class RestaurantController {
    private final RestaurantService restaurantService;
    private final RestaurantAdminRepository restaurantAdminRepository;
    private final ObjectMapper objectMapper;
    @GetMapping("/my-orders")
    @PreAuthorize("hasAuthority('ROLE_RESTAURANT_ADMIN')")
    public List<OrderDto> getMyOrders(Authentication authentication) {
        Long userId = Long.parseLong((String) authentication.getPrincipal());
        RestaurantAdmin admin = restaurantAdminRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Restaurant admin not found"));
        return this.restaurantService.getAllOrders(admin.getRestaurant());

    }

    @PostMapping(value = "/addMenu", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAuthority('ROLE_RESTAURANT_ADMIN')")
    public MenuItem addMenuItem(
            Authentication authentication,
            @RequestPart("menu") String menuJson,
            @RequestPart(value = "files", required = false) List<MultipartFile> files) throws IOException {

        // 1. Get restaurant admin from authentication
        RestaurantAdmin restaurantAdmin = restaurantAdminRepository.findById(
                Long.parseLong((String) authentication.getPrincipal())
        ).orElseThrow(() -> new RuntimeException("Restaurant admin not found"));

        // 2. Parse DTO
        MenuItemRequestDTO menuDto = objectMapper.readValue(menuJson, MenuItemRequestDTO.class);
        menuDto.setRestaurantId(restaurantAdmin.getRestaurant().getId());

        // 3. Call service (empty list if no files provided)
        return restaurantService.addMenu(menuDto, files != null ? files : new ArrayList<>());
    }

    @GetMapping("/my-menu")
    @PreAuthorize("hasAuthority('ROLE_RESTAURANT_ADMIN')")
    public List<MenuItem> getMyMenu(Authentication authentication) {
        RestaurantAdmin restaurantAdmin = this.restaurantAdminRepository.findById(Long.parseLong((String) authentication.getPrincipal()))
                .orElseThrow(() -> new RuntimeException("Restaurant admin not found"));
        return restaurantAdmin.getRestaurant().getMenu();
    }

    @GetMapping("/order/{id}")
    @PreAuthorize("hasAuthority('ROLE_RESTAURANT_ADMIN')")
    public OrderDto getOrder(@PathVariable Long id, Authentication authentication) {
        Long userId = Long.parseLong((String) authentication.getPrincipal());
        Long restaurantId = restaurantAdminRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Restaurant admin not found"))
                .getRestaurant()
                .getId();
        return this.restaurantService.getOrderForRestaurant(id, restaurantId);
    }

    @PostMapping("/accept-order/{id}")
    @PreAuthorize("hasAuthority('ROLE_RESTAURANT_ADMIN')")
    public OrderDto acceptOrder(Authentication authentication, @PathVariable Long id) {
        Long userId = Long.parseLong((String) authentication.getPrincipal());
        return this.restaurantService.acceptOrder(id, userId);

    }

    @PostMapping("/order/ready/{id}")
    @PreAuthorize("hasAuthority('ROLE_RESTAURANT_ADMIN')")
    public OrderDto readyOrder(Authentication authentication, @PathVariable Long id) {
        Long userId = Long.parseLong((String) authentication.getPrincipal());
        return this.restaurantService.markOrderReady(id, userId);
    }


}
