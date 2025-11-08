package com.foodify.server.modules.restaurants.api;

import com.foodify.server.modules.customers.application.ClientService;
import com.foodify.server.modules.customers.dto.ClientFavoriteIds;
import com.foodify.server.modules.restaurants.application.RestaurantDeliveryFeeService;
import com.foodify.server.modules.restaurants.application.RestaurantSearchService;
import com.foodify.server.modules.restaurants.domain.RestaurantCategory;
import com.foodify.server.modules.restaurants.dto.PageResponse;
import com.foodify.server.modules.restaurants.dto.DeliveryFeeResponse;
import com.foodify.server.modules.restaurants.dto.RestaurantSearchItemDto;
import com.foodify.server.modules.restaurants.dto.RestaurantSearchQuery;
import com.foodify.server.modules.restaurants.dto.RestaurantSearchSort;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import org.springframework.security.core.Authentication;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;
import java.util.LinkedHashSet;
import java.util.Locale;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/client/restaurants")
public class ClientRestaurantController {

    private final RestaurantSearchService restaurantSearchService;
    private final RestaurantDeliveryFeeService restaurantDeliveryFeeService;
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
            @RequestParam(required = false, name = "categories") Set<String> categoryFilters,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate clientDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime clientTime,
            Authentication authentication
    ) {
        int effectivePage = page != null && page > 0 ? page : 1;
        int effectivePageSize = pageSize != null && pageSize > 0 ? pageSize : 20;
        Set<RestaurantCategory> categories = parseCategories(categoryFilters);
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
                categories,
                effectivePage,
                effectivePageSize,
                clientDate,
                clientTime
        );
        ClientFavoriteIds favoriteIds = resolveFavoriteIds(authentication);
        return restaurantSearchService.search(searchQuery, favoriteIds.restaurantIds(), favoriteIds.menuItemIds());
    }

    @GetMapping("/{restaurantId}/delivery-fee")
    public DeliveryFeeResponse getDeliveryFee(
            @PathVariable Long restaurantId,
            @RequestParam Double lat,
            @RequestParam Double lng
    ) {
        if (lat == null || lng == null) {
            throw new ResponseStatusException(BAD_REQUEST, "Latitude and longitude are required");
        }
        return restaurantDeliveryFeeService.calculateDeliveryFee(restaurantId, lat, lng);
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

    private Set<RestaurantCategory> parseCategories(Set<String> categories) {
        if (categories == null || categories.isEmpty()) {
            return Set.of();
        }
        Set<RestaurantCategory> parsed = new LinkedHashSet<>();
        for (String value : categories) {
            if (!StringUtils.hasText(value)) {
                continue;
            }
            String[] tokens = value.split(",");
            for (String token : tokens) {
                String normalized = token.trim();
                if (!StringUtils.hasText(normalized)) {
                    continue;
                }
                try {
                    parsed.add(RestaurantCategory.valueOf(normalized.toUpperCase(Locale.ROOT)));
                } catch (IllegalArgumentException ex) {
                    throw new ResponseStatusException(BAD_REQUEST, "Unknown restaurant category: " + normalized);
                }
            }
        }
        return parsed;
    }
}
