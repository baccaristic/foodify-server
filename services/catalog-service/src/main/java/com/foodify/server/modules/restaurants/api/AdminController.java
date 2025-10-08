package com.foodify.server.modules.restaurants.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.foodify.server.modules.restaurants.dto.RestaurantDto;
import com.foodify.server.modules.restaurants.domain.Restaurant;
import com.foodify.server.modules.restaurants.application.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class AdminController {
    private final AdminService adminService;
    private final ObjectMapper objectMapper;

    @PostMapping(value = "/restaurant/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public Restaurant addRestaurant(
            @RequestPart("restaurant") String restaurantJson,
            @RequestPart("file") MultipartFile file
    ) throws IOException {
        RestaurantDto dto = objectMapper.readValue(restaurantJson, RestaurantDto.class);
        return this.adminService.addRestaurant(dto, file);
    }
}
