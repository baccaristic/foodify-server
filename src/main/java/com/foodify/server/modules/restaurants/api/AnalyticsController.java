package com.foodify.server.modules.restaurants.api;

import com.foodify.server.modules.identity.domain.RestaurantAdmin;
import com.foodify.server.modules.identity.domain.RestaurantCashier;
import com.foodify.server.modules.identity.repository.RestaurantAdminRepository;
import com.foodify.server.modules.identity.repository.RestaurantCashierRepository;
import com.foodify.server.modules.restaurants.application.AnalyticsService;
import com.foodify.server.modules.restaurants.dto.RestaurantUserContext;
import com.foodify.server.modules.restaurants.dto.analytics.AnalyticsPeriod;
import com.foodify.server.modules.restaurants.dto.analytics.GeneralOverviewResponse;
import com.foodify.server.modules.restaurants.dto.analytics.SalesTrendResponse;
import com.foodify.server.modules.restaurants.dto.analytics.TopDishesResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/restaurant/analytics")
@RequiredArgsConstructor
public class AnalyticsController {

    private final AnalyticsService analyticsService;
    private final RestaurantAdminRepository restaurantAdminRepository;
    private final RestaurantCashierRepository restaurantCashierRepository;

    @GetMapping("/overview")
    @PreAuthorize("hasAnyAuthority('ROLE_RESTAURANT_ADMIN', 'ROLE_RESTAURANT_CASHIER')")
    public GeneralOverviewResponse getGeneralOverview(
            Authentication authentication,
            @RequestParam(defaultValue = "TODAY") AnalyticsPeriod period
    ) {
        RestaurantUserContext userContext = loadRestaurantUser(authentication);
        return analyticsService.getGeneralOverview(userContext.getRestaurant().getId(), period);
    }

    @GetMapping("/sales-trend")
    @PreAuthorize("hasAnyAuthority('ROLE_RESTAURANT_ADMIN', 'ROLE_RESTAURANT_CASHIER')")
    public SalesTrendResponse getSalesTrend(
            Authentication authentication,
            @RequestParam(defaultValue = "TODAY") AnalyticsPeriod period
    ) {
        RestaurantUserContext userContext = loadRestaurantUser(authentication);
        return analyticsService.getSalesTrend(userContext.getRestaurant().getId(), period);
    }

    @GetMapping("/top-dishes")
    @PreAuthorize("hasAnyAuthority('ROLE_RESTAURANT_ADMIN', 'ROLE_RESTAURANT_CASHIER')")
    public TopDishesResponse getTopDishes(
            Authentication authentication,
            @RequestParam(defaultValue = "TODAY") AnalyticsPeriod period
    ) {
        RestaurantUserContext userContext = loadRestaurantUser(authentication);
        return analyticsService.getTopDishes(userContext.getRestaurant().getId(), period);
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
}
