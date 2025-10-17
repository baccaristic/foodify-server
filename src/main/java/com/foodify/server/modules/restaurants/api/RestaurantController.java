package com.foodify.server.modules.restaurants.api;

import com.foodify.server.modules.orders.dto.OrderNotificationDTO;
import com.foodify.server.modules.restaurants.domain.MenuCategory;
import com.foodify.server.modules.restaurants.domain.MenuItem;
import com.foodify.server.modules.restaurants.dto.MenuCategoryRequestDTO;
import com.foodify.server.modules.restaurants.dto.MenuItemAvailabilityRequest;
import com.foodify.server.modules.restaurants.dto.MenuItemRequestDTO;
import com.foodify.server.modules.restaurants.dto.PageResponse;
import com.foodify.server.modules.identity.domain.RestaurantAdmin;
import com.foodify.server.modules.identity.repository.RestaurantAdminRepository;
import com.foodify.server.modules.restaurants.application.RestaurantService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/restaurant")
@RequiredArgsConstructor
public class RestaurantController {
    private final RestaurantService restaurantService;
    private final RestaurantAdminRepository restaurantAdminRepository;
    @GetMapping("/my-orders")
    @PreAuthorize("hasAuthority('ROLE_RESTAURANT_ADMIN')")
    public PageResponse<OrderNotificationDTO> getMyOrders(
            Authentication authentication,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int pageSize,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime from,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime to
    ) {
        RestaurantAdmin admin = loadAdmin(authentication);
        int effectivePage = Math.max(page, 0);
        int effectivePageSize = pageSize > 0 ? Math.min(pageSize, 50) : 20;

        PageRequest pageRequest = PageRequest.of(effectivePage, effectivePageSize, Sort.by("date").descending());

        Page<OrderNotificationDTO> orders = this.restaurantService.getAllOrders(
                admin.getRestaurant(),
                pageRequest,
                from,
                to
        );

        return new PageResponse<>(
                orders.getContent(),
                orders.getNumber(),
                orders.getSize(),
                orders.getTotalElements()
        );
    }

    @GetMapping("/categories")
    @PreAuthorize("hasAuthority('ROLE_RESTAURANT_ADMIN')")
    public List<MenuCategory> getCategories(Authentication authentication) {
        RestaurantAdmin restaurantAdmin = loadAdmin(authentication);
        return restaurantService.getCategoriesForRestaurant(restaurantAdmin.getRestaurant().getId());
    }

    @PostMapping("/categories")
    @PreAuthorize("hasAuthority('ROLE_RESTAURANT_ADMIN')")
    public MenuCategory createCategory(Authentication authentication, @RequestBody MenuCategoryRequestDTO request) {
        RestaurantAdmin restaurantAdmin = loadAdmin(authentication);
        return restaurantService.createCategory(restaurantAdmin.getRestaurant().getId(), request);
    }

    @GetMapping("/my-active-orders")
    @PreAuthorize("hasAuthority('ROLE_RESTAURANT_ADMIN')")
    public List<OrderNotificationDTO> getMyActiveOrders(Authentication authentication) {
        RestaurantAdmin admin = loadAdmin(authentication);
        return this.restaurantService.getActiveOrders(admin.getId());
    }

    @PostMapping(value = "/addMenu", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAuthority('ROLE_RESTAURANT_ADMIN')")
    public MenuItem addMenuItem(
            Authentication authentication,
            @Parameter(
                    description = "Menu details payload",
                    required = true,
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = MenuItemRequestDTO.class))
            )
            @RequestPart("menu") MenuItemRequestDTO menuDto,
            @RequestPart(value = "files", required = false) List<MultipartFile> files) throws IOException {

        // 1. Get restaurant admin from authentication
        RestaurantAdmin restaurantAdmin = loadAdmin(authentication);

        // 2. Enrich DTO with restaurant context
        menuDto.setRestaurantId(restaurantAdmin.getRestaurant().getId());

        // 3. Call service (empty list if no files provided)
        return restaurantService.addMenu(menuDto, files != null ? files : new ArrayList<>());
    }

    @PutMapping(value = "/menu/{menuId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAuthority('ROLE_RESTAURANT_ADMIN')")
    public MenuItem updateMenuItem(
            Authentication authentication,
            @PathVariable Long menuId,
            @Parameter(
                    description = "Menu details payload",
                    required = true,
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = MenuItemRequestDTO.class))
            )
            @RequestPart("menu") MenuItemRequestDTO menuDto,
            @RequestPart(value = "files", required = false) List<MultipartFile> files) throws IOException {

        RestaurantAdmin restaurantAdmin = loadAdmin(authentication);

        menuDto.setId(menuId);
        menuDto.setRestaurantId(restaurantAdmin.getRestaurant().getId());

        return restaurantService.updateMenu(menuId, menuDto, files != null ? files : new ArrayList<>());
    }

    @PatchMapping("/menu/{menuId}/availability")
    @PreAuthorize("hasAuthority('ROLE_RESTAURANT_ADMIN')")
    public MenuItem updateMenuAvailability(
            Authentication authentication,
            @PathVariable Long menuId,
            @RequestBody MenuItemAvailabilityRequest request) {

        RestaurantAdmin restaurantAdmin = loadAdmin(authentication);
        return restaurantService.updateMenuAvailability(
                menuId,
                restaurantAdmin.getRestaurant().getId(),
                request.available()
        );
    }

    @GetMapping("/my-menu")
    @PreAuthorize("hasAuthority('ROLE_RESTAURANT_ADMIN')")
    public List<MenuItem> getMyMenu(Authentication authentication) {
        RestaurantAdmin restaurantAdmin = loadAdmin(authentication);
        return restaurantAdmin.getRestaurant().getMenu();
    }

    @GetMapping("/order/{id}")
    @PreAuthorize("hasAuthority('ROLE_RESTAURANT_ADMIN')")
    public OrderNotificationDTO getOrder(@PathVariable Long id, Authentication authentication) {
        RestaurantAdmin admin = loadAdmin(authentication);
        Long restaurantId = admin.getRestaurant().getId();
        return this.restaurantService.getOrderForRestaurant(id, restaurantId);
    }

    @PostMapping("/accept-order/{id}")
    @PreAuthorize("hasAuthority('ROLE_RESTAURANT_ADMIN')")
    public OrderNotificationDTO acceptOrder(Authentication authentication, @PathVariable Long id) {
        Long userId = Long.parseLong((String) authentication.getPrincipal());
        return this.restaurantService.acceptOrder(id, userId);

    }

    @PostMapping("/order/ready/{id}")
    @PreAuthorize("hasAuthority('ROLE_RESTAURANT_ADMIN')")
    public OrderNotificationDTO readyOrder(Authentication authentication, @PathVariable Long id) {
        Long userId = Long.parseLong((String) authentication.getPrincipal());
        return this.restaurantService.markOrderReady(id, userId);
    }

    private RestaurantAdmin loadAdmin(Authentication authentication) {
        Long userId = Long.parseLong((String) authentication.getPrincipal());
        return restaurantAdminRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Restaurant admin not found"));
    }
}
