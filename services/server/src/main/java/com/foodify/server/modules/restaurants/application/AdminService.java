package com.foodify.server.modules.restaurants.application;

import com.foodify.server.modules.identity.domain.Driver;
import com.foodify.server.modules.identity.domain.RestaurantAdmin;
import com.foodify.server.modules.identity.domain.Role;
import com.foodify.server.modules.identity.repository.RestaurantAdminRepository;
import com.foodify.server.modules.identity.repository.UserRepository;
import com.foodify.server.modules.media.ImageStorageClient;
import com.foodify.server.modules.restaurants.domain.Restaurant;
import com.foodify.server.modules.restaurants.dto.NewUserDto;
import com.foodify.server.modules.restaurants.dto.RestaurantDto;
import com.foodify.server.modules.restaurants.mapper.RestaurantMapper;
import com.foodify.server.modules.restaurants.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashSet;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final UserRepository userRepository;
    private final RestaurantRepository restaurantRepository;
    private final RestaurantMapper restaurantMapper;
    private final RestaurantAdminRepository restaurantAdminRepository;
    private final PasswordEncoder passwordEncoder;
    private final ImageStorageClient imageStorageClient;

    public Driver addDriver(Driver driver) {
        return null;
    }

    public Restaurant addRestaurant(RestaurantDto dto, MultipartFile image, MultipartFile icon) throws IOException {
        if (dto == null) {
            throw new ResponseStatusException(BAD_REQUEST, "Restaurant payload is required");
        }

        Restaurant restaurant = this.restaurantMapper.toEntity(dto);
        if (dto.getCategories() != null) {
            restaurant.setCategories(new HashSet<>(dto.getCategories()));
        }
        restaurant.setRestaurantShareRate(resolveShareRate(dto.getRestaurantShareRate(), null));

        if (image == null || image.isEmpty()) {
            throw new ResponseStatusException(BAD_REQUEST, "Restaurant image is required");
        }

        restaurant.setImageUrl(storeFile(image));

        if (icon != null && !icon.isEmpty()) {
            restaurant.setIconUrl(storeFile(icon));
        }

        NewUserDto adminPayload = dto.getAdmin();
        if (adminPayload == null
                || !StringUtils.hasText(adminPayload.getEmail())
                || !StringUtils.hasText(adminPayload.getPassword())
                || !StringUtils.hasText(adminPayload.getName())) {
            throw new ResponseStatusException(BAD_REQUEST, "Administrator credentials (name, email, password) are required");
        }

        RestaurantAdmin restaurantAdmin = new RestaurantAdmin();
        restaurantAdmin.setName(adminPayload.getName());
        restaurantAdmin.setEmail(adminPayload.getEmail());
        restaurantAdmin.setPassword(this.passwordEncoder.encode(adminPayload.getPassword()));
        restaurantAdmin.setRole(Role.RESTAURANT_ADMIN);
        restaurantAdmin.setEnabled(true);
        restaurant.setAdmin(restaurantAdminRepository.save(restaurantAdmin));

        return this.restaurantRepository.save(restaurant);
    }

    public Restaurant updateRestaurant(Long restaurantId, RestaurantDto dto, MultipartFile image, MultipartFile icon) throws IOException {
        if (dto == null) {
            throw new ResponseStatusException(BAD_REQUEST, "Restaurant payload is required");
        }

        Restaurant restaurant = this.restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Restaurant not found"));

        BigDecimal previousShareRate = restaurant.getRestaurantShareRate();
        this.restaurantMapper.updateEntity(dto, restaurant);
        if (dto.getCategories() != null) {
            restaurant.setCategories(new HashSet<>(dto.getCategories()));
        }
        restaurant.setRestaurantShareRate(resolveShareRate(dto.getRestaurantShareRate(), previousShareRate));

        if (image != null && !image.isEmpty()) {
            restaurant.setImageUrl(storeFile(image));
        }

        if (icon != null && !icon.isEmpty()) {
            restaurant.setIconUrl(storeFile(icon));
        }

        if (dto.getAdmin() != null) {
            RestaurantAdmin restaurantAdmin = restaurant.getAdmin();
            if (restaurantAdmin == null) {
                restaurantAdmin = new RestaurantAdmin();
                restaurantAdmin.setRole(Role.RESTAURANT_ADMIN);
                restaurantAdmin.setEnabled(true);
            }

            NewUserDto adminPayload = dto.getAdmin();
            if (StringUtils.hasText(adminPayload.getName())) {
                restaurantAdmin.setName(adminPayload.getName());
            }
            if (StringUtils.hasText(adminPayload.getEmail())) {
                restaurantAdmin.setEmail(adminPayload.getEmail());
            }
            if (StringUtils.hasText(adminPayload.getPassword())) {
                restaurantAdmin.setPassword(this.passwordEncoder.encode(adminPayload.getPassword()));
            }

            restaurant.setAdmin(restaurantAdminRepository.save(restaurantAdmin));
        }

        return this.restaurantRepository.save(restaurant);
    }

    private String storeFile(MultipartFile file) throws IOException {
        return imageStorageClient.store(file);
    }

    private BigDecimal resolveShareRate(BigDecimal requestedRate, BigDecimal fallbackRate) {
        BigDecimal defaultRestaurantShare = BigDecimal.valueOf(0.88).setScale(4, RoundingMode.HALF_UP);
        if (requestedRate != null) {
            return requestedRate.setScale(4, RoundingMode.HALF_UP);
        }
        if (fallbackRate != null) {
            return fallbackRate.setScale(4, RoundingMode.HALF_UP);
        }
        return defaultRestaurantShare;
    }
}
