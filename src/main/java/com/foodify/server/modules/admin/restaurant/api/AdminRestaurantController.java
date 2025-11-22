package com.foodify.server.modules.admin.restaurant.api;


import com.foodify.server.modules.admin.restaurant.application.AdminRestaurantService;
import com.foodify.server.modules.admin.restaurant.dto.AddRestaurantDto;
import com.foodify.server.modules.admin.restaurant.dto.RestaurantDto;
import com.foodify.server.modules.restaurants.domain.Restaurant;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class AdminRestaurantController {

    private final AdminRestaurantService adminRestaurantService;

    @PostMapping(value = {"/restaurants/add"}, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public Restaurant addRestaurant(
            @RequestPart("restaurant") @Valid AddRestaurantDto restaurantDto,
            @RequestPart("image") MultipartFile image,
            @RequestPart(value = "icon", required = false) MultipartFile icon
    ) throws IOException {
        return this.adminRestaurantService.addRestaurant(restaurantDto, image, icon);
    }

    @GetMapping("/restaurants")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public Page<RestaurantDto> getRestaurants(
            @RequestParam(required = false) String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        return this.adminRestaurantService.searchRestaurants(query, page, size);
    }
}
