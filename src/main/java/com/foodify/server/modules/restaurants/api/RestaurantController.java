package com.foodify.server.modules.restaurants.api;

import com.foodify.server.modules.orders.dto.OrderNotificationDTO;
import com.foodify.server.modules.restaurants.domain.MenuCategory;
import com.foodify.server.modules.restaurants.domain.MenuItem;
import com.foodify.server.modules.restaurants.dto.MenuCategoryRequestDTO;
import com.foodify.server.modules.restaurants.dto.MenuItemAvailabilityRequest;
import com.foodify.server.modules.restaurants.dto.MenuItemRequestDTO;
import com.foodify.server.modules.restaurants.dto.OperatingHoursResponse;
import com.foodify.server.modules.restaurants.dto.PageResponse;
import com.foodify.server.modules.restaurants.dto.SaveSpecialDayRequest;
import com.foodify.server.modules.restaurants.dto.UpdatePreparationEstimateRequest;
import com.foodify.server.modules.restaurants.dto.UpdateWeeklyScheduleRequest;
import com.foodify.server.modules.identity.domain.RestaurantAdmin;
import com.foodify.server.modules.identity.domain.RestaurantCashier;
import com.foodify.server.modules.identity.domain.Role;
import com.foodify.server.modules.identity.repository.RestaurantAdminRepository;
import com.foodify.server.modules.identity.repository.RestaurantCashierRepository;
import com.foodify.server.modules.restaurants.application.RestaurantService;
import com.foodify.server.modules.restaurants.dto.RestaurantUserContext;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import jakarta.validation.Valid;
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
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/restaurant")
@RequiredArgsConstructor
public class RestaurantController {
    private final RestaurantService restaurantService;
    private final RestaurantAdminRepository restaurantAdminRepository;
    private final RestaurantCashierRepository restaurantCashierRepository;
    @GetMapping("/my-orders")
    @PreAuthorize("hasAnyAuthority('ROLE_RESTAURANT_ADMIN', 'ROLE_RESTAURANT_CASHIER')")
    public PageResponse<OrderNotificationDTO> getMyOrders(
            Authentication authentication,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int pageSize,
            @RequestParam(required = false)
            @DateTimeFormat(pattern = "dd/MM/yyyy")
            LocalDate from,
            @RequestParam(required = false)
            @DateTimeFormat(pattern = "dd/MM/yyyy")
            LocalDate to
    ) {
        RestaurantUserContext userContext = loadRestaurantUser(authentication);
        int effectivePage = Math.max(page, 0);
        int effectivePageSize = pageSize > 0 ? Math.min(pageSize, 50) : 20;

        PageRequest pageRequest = PageRequest.of(effectivePage, effectivePageSize, Sort.by("date").descending());

        Page<OrderNotificationDTO> orders = this.restaurantService.getAllOrders(
                userContext.getRestaurant(),
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
    @PreAuthorize("hasAnyAuthority('ROLE_RESTAURANT_ADMIN', 'ROLE_RESTAURANT_CASHIER')")
    public List<MenuCategory> getCategories(Authentication authentication) {
        RestaurantUserContext userContext = loadRestaurantUser(authentication);
        return restaurantService.getCategoriesForRestaurant(userContext.getRestaurant().getId());
    }

    @GetMapping("/operating-hours")
    @PreAuthorize("hasAnyAuthority('ROLE_RESTAURANT_ADMIN', 'ROLE_RESTAURANT_CASHIER')")
    public OperatingHoursResponse getOperatingHours(Authentication authentication) {
        RestaurantUserContext userContext = loadRestaurantUser(authentication);
        return restaurantService.getOperatingHours(userContext.getRestaurant().getId());
    }

    @PutMapping("/operating-hours/weekly")
    @PreAuthorize("hasAuthority('ROLE_RESTAURANT_ADMIN')")
    public OperatingHoursResponse updateWeeklySchedule(
            Authentication authentication,
            @RequestBody UpdateWeeklyScheduleRequest request
    ) {
        RestaurantAdmin restaurantAdmin = loadAdmin(authentication);
        return restaurantService.updateWeeklySchedule(restaurantAdmin.getRestaurant().getId(), request);
    }

    @PostMapping("/operating-hours/special-days")
    @PreAuthorize("hasAuthority('ROLE_RESTAURANT_ADMIN')")
    public OperatingHoursResponse.SpecialDay addSpecialDay(
            Authentication authentication,
            @RequestBody SaveSpecialDayRequest request
    ) {
        RestaurantAdmin restaurantAdmin = loadAdmin(authentication);
        return restaurantService.addSpecialDay(restaurantAdmin.getRestaurant().getId(), request);
    }

    @PutMapping("/operating-hours/special-days/{id}")
    @PreAuthorize("hasAuthority('ROLE_RESTAURANT_ADMIN')")
    public OperatingHoursResponse.SpecialDay updateSpecialDay(
            Authentication authentication,
            @PathVariable Long id,
            @RequestBody SaveSpecialDayRequest request
    ) {
        RestaurantAdmin restaurantAdmin = loadAdmin(authentication);
        return restaurantService.updateSpecialDay(restaurantAdmin.getRestaurant().getId(), id, request);
    }

    @DeleteMapping("/operating-hours/special-days/{id}")
    @PreAuthorize("hasAuthority('ROLE_RESTAURANT_ADMIN')")
    public void deleteSpecialDay(Authentication authentication, @PathVariable Long id) {
        RestaurantAdmin restaurantAdmin = loadAdmin(authentication);
        restaurantService.deleteSpecialDay(restaurantAdmin.getRestaurant().getId(), id);
    }

    @PostMapping("/categories")
    @PreAuthorize("hasAuthority('ROLE_RESTAURANT_ADMIN')")
    public MenuCategory createCategory(Authentication authentication, @RequestBody MenuCategoryRequestDTO request) {
        RestaurantAdmin restaurantAdmin = loadAdmin(authentication);
        return restaurantService.createCategory(restaurantAdmin.getRestaurant().getId(), request);
    }

    @GetMapping("/my-active-orders")
    @PreAuthorize("hasAnyAuthority('ROLE_RESTAURANT_ADMIN', 'ROLE_RESTAURANT_CASHIER')")
    public List<OrderNotificationDTO> getMyActiveOrders(Authentication authentication) {
        RestaurantUserContext userContext = loadRestaurantUser(authentication);
        return this.restaurantService.getActiveOrders(userContext.getRestaurant());
    }

    @GetMapping("/points/balance")
    @PreAuthorize("hasAnyAuthority('ROLE_RESTAURANT_ADMIN', 'ROLE_RESTAURANT_CASHIER')")
    public BigDecimal getPointsBalance(Authentication authentication) {
        RestaurantUserContext userContext = loadRestaurantUser(authentication);
        return userContext.getRestaurant().getPointsBalance();
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
    @PreAuthorize("hasAnyAuthority('ROLE_RESTAURANT_ADMIN', 'ROLE_RESTAURANT_CASHIER')")
    public MenuItem updateMenuAvailability(
            Authentication authentication,
            @PathVariable Long menuId,
            @RequestBody MenuItemAvailabilityRequest request) {

        RestaurantUserContext userContext = loadRestaurantUser(authentication);
        return restaurantService.updateMenuAvailability(
                menuId,
                userContext.getRestaurant().getId(),
                request.available()
        );
    }

    @GetMapping("/my-menu")
    @PreAuthorize("hasAnyAuthority('ROLE_RESTAURANT_ADMIN', 'ROLE_RESTAURANT_CASHIER')")
    public List<MenuItem> getMyMenu(Authentication authentication) {
        RestaurantUserContext userContext = loadRestaurantUser(authentication);
        return userContext.getRestaurant().getMenu();
    }

    @GetMapping("/order/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_RESTAURANT_ADMIN', 'ROLE_RESTAURANT_CASHIER')")
    public OrderNotificationDTO getOrder(@PathVariable Long id, Authentication authentication) {
        RestaurantUserContext userContext = loadRestaurantUser(authentication);
        Long restaurantId = userContext.getRestaurant().getId();
        return this.restaurantService.getOrderForRestaurant(id, restaurantId);
    }

    @PostMapping("/accept-order/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_RESTAURANT_ADMIN', 'ROLE_RESTAURANT_CASHIER')")
    public OrderNotificationDTO acceptOrder(Authentication authentication, @PathVariable Long id) {
        Long userId = Long.parseLong((String) authentication.getPrincipal());
        return this.restaurantService.acceptOrder(id, userId);

    }

    @PostMapping("/order/{id}/estimate")
    @PreAuthorize("hasAnyAuthority('ROLE_RESTAURANT_ADMIN', 'ROLE_RESTAURANT_CASHIER')")
    public OrderNotificationDTO updateOrderEstimate(
            Authentication authentication,
            @PathVariable Long id,
            @RequestBody @Valid UpdatePreparationEstimateRequest request
    ) {
        Long userId = Long.parseLong((String) authentication.getPrincipal());
        Integer minutes = request != null ? request.minutes() : null;
        return this.restaurantService.updatePreparationEstimate(id, userId, minutes);
    }

    @PostMapping("/order/{id}/start-preparing")
    @PreAuthorize("hasAnyAuthority('ROLE_RESTAURANT_ADMIN', 'ROLE_RESTAURANT_CASHIER')")
    public OrderNotificationDTO startPreparingOrder(
            Authentication authentication,
            @PathVariable Long id,
            @RequestBody @Valid UpdatePreparationEstimateRequest request
    ) {
        Long userId = Long.parseLong((String) authentication.getPrincipal());
        return this.restaurantService.startPreparingOrder(id, userId, request.minutes());
    }

    @PostMapping("/order/ready/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_RESTAURANT_ADMIN', 'ROLE_RESTAURANT_CASHIER')")
    public OrderNotificationDTO readyOrder(Authentication authentication, @PathVariable Long id) {
        Long userId = Long.parseLong((String) authentication.getPrincipal());
        return this.restaurantService.markOrderReady(id, userId);
    }

    private RestaurantUserContext loadRestaurantUser(Authentication authentication) {
        Long userId = Long.parseLong((String) authentication.getPrincipal());
        
        // First try to load as RestaurantAdmin
        RestaurantAdmin admin = restaurantAdminRepository.findById(userId).orElse(null);
        if (admin != null) {
            return new RestaurantUserContext(userId, admin.getRestaurant());
        }
        
        // If not found as admin, try loading as RestaurantCashier
        RestaurantCashier cashier = restaurantCashierRepository.findById(userId).orElse(null);
        if (cashier != null) {
            return new RestaurantUserContext(userId, cashier.getRestaurant());
        }
        
        throw new RuntimeException("Restaurant user not found");
    }
    
    /**
     * @deprecated Use loadRestaurantUser instead for compatibility with both admin and cashier roles
     */
    @Deprecated
    private RestaurantAdmin loadAdmin(Authentication authentication) {
        Long userId = Long.parseLong((String) authentication.getPrincipal());
        return restaurantAdminRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Restaurant admin not found"));
    }
}
