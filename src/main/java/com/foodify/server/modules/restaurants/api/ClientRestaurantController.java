package com.foodify.server.modules.restaurants.api;

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

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/client/restaurants")
public class ClientRestaurantController {

    private final RestaurantSearchService restaurantSearchService;

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
            @RequestParam(required = false) Integer pageSize
    ) {
        RestaurantSearchQuery searchQuery = new RestaurantSearchQuery(
                query,
                hasPromotion,
                isTopChoice,
                hasFreeDelivery,
                RestaurantSearchSort.fromNullable(sort),
                topEatOnly,
                maxDeliveryFee,
                page,
                pageSize
        );
        return restaurantSearchService.search(searchQuery);
    }
}
