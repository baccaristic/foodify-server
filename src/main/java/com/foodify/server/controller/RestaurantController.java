package com.foodify.server.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.foodify.server.dto.MenuItemRequestDTO;
import com.foodify.server.enums.OrderStatus;
import com.foodify.server.models.*;
import com.foodify.server.repository.RestaurantAdminRepository;
import com.foodify.server.service.RestaurantService;
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
    public List<Order> getMyOrders(Authentication authentication) {
        Long userId = Long.parseLong((String) authentication.getPrincipal());
        return this.restaurantService.getAllOrders(this.restaurantAdminRepository.findById(userId).orElse(null).getRestaurant());

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
        RestaurantAdmin restaurantAdmin = this.restaurantAdminRepository.findById(Long.parseLong((String) authentication.getPrincipal())).orElse(null);
        return restaurantAdmin.getRestaurant().getMenu();
    }

    @GetMapping("/order/{id}")
    @PreAuthorize("hasAuthority('ROLE_RESTAURANT_ADMIN')")
    public Order getOrder(@PathVariable Long id, Authentication authentication) {
        Long userId = Long.parseLong((String) authentication.getPrincipal());
        Order order = this.restaurantService.getOrderById(id);
        if (order == null || order.getRestaurant().getId() != this.restaurantAdminRepository.findById(userId).orElse(null).getRestaurant().getId()) {
            return null;
        }
        return  order;
    }

    @PostMapping("/accept-order/{id}")
    @PreAuthorize("hasAuthority('ROLE_RESTAURANT_ADMIN')")
    public Order acceptOrder(Authentication authentication, @PathVariable Long id) {
        Long userId = Long.parseLong((String) authentication.getPrincipal());
        return this.restaurantService.acceptOrder(id, userId);

    }

    @PostMapping("/order/ready/{id}")
    @PreAuthorize("hasAuthority('ROLE_RESTAURANT_ADMIN')")
    public Order readyOrder(Authentication authentication, @PathVariable Long id) {
        Long userId = Long.parseLong((String) authentication.getPrincipal());
        return this.restaurantService.markOrderReady(id, userId);
    }


}
