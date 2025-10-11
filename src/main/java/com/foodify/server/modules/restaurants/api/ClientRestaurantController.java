package com.foodify.server.modules.restaurants.api;

import com.foodify.server.modules.customers.application.ClientService;
import com.foodify.server.modules.customers.dto.ClientFavoriteIds;
import com.foodify.server.modules.restaurants.application.RestaurantSearchService;
import com.foodify.server.modules.restaurants.dto.PageResponse;
import com.foodify.server.modules.restaurants.dto.RestaurantSearchItemDto;
import com.foodify.server.modules.restaurants.dto.RestaurantSearchQuery;
import com.foodify.server.modules.restaurants.dto.RestaurantSearchSort;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.security.core.Authentication;

import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/client/restaurants")
public class ClientRestaurantController {

    private final RestaurantSearchService restaurantSearchService;
    private final ClientService clientService;

    @GetMapping("/search")
    public PageResponse<RestaurantSearchItemDto> search(
            @RequestParam(defaultValue = "") String query,
            @RequestParam(required = false) Boolean hasPromotion,
            @RequestParam(required = false) Boolean isTopChoice,
            @RequestParam(required = false) Boolean hasFreeDelivery,
            @RequestParam(required = false) String sort,
            @RequestParam(required = false) Boolean topEatOnly,
            @RequestParam(required = false) Double maxDeliveryFee,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer pageSize,
            @RequestParam(required = false) Double lat,
            @RequestParam(required = false) Double lng,
            Authentication authentication
    ) {
        int effectivePage = page != null && page > 0 ? page : 1;
        int effectivePageSize = pageSize != null && pageSize > 0 ? pageSize : 20;
        RestaurantSearchQuery searchQuery = new RestaurantSearchQuery(
                query,
                hasPromotion,
                isTopChoice,
                hasFreeDelivery,
                RestaurantSearchSort.fromNullable(sort),
                topEatOnly,
                maxDeliveryFee,
                lat,
                lng,
                effectivePage,
                effectivePageSize
        );
        ClientFavoriteIds favoriteIds = resolveFavoriteIds(authentication);
        return restaurantSearchService.search(searchQuery, favoriteIds.restaurantIds(), favoriteIds.menuItemIds());
    }

    private ClientFavoriteIds resolveFavoriteIds(Authentication authentication) {
        if (authentication == null) {
            return new ClientFavoriteIds(Set.of(), Set.of());
        }
        Long userId = extractUserId(authentication);
        if (userId == null) {
            return new ClientFavoriteIds(Set.of(), Set.of());
        }
        return clientService.getFavoriteIds(userId);
    }

    private Long extractUserId(Authentication authentication) {
        Object principal = authentication.getPrincipal();
        if (principal instanceof String principalStr) {
            return Long.parseLong(principalStr);
        }
        return null;
    }
}
