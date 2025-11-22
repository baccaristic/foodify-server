package com.foodify.server.modules.restaurants.api;

import com.foodify.server.modules.restaurants.application.AdminService;
import com.foodify.server.modules.restaurants.domain.Restaurant;
import com.foodify.server.modules.restaurants.dto.RestaurantDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService adminService;

    @PutMapping(value = "/restaurants/{restaurantId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public Restaurant updateRestaurant(
            @PathVariable Long restaurantId,
            @RequestPart("restaurant") @Valid RestaurantDto restaurantDto,
            @RequestPart(value = "image", required = false) MultipartFile image,
            @RequestPart(value = "icon", required = false) MultipartFile icon
    ) throws IOException {
        return this.adminService.updateRestaurant(restaurantId, restaurantDto, image, icon);
    }
}
