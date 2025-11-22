package com.foodify.server.modules.admin.restaurant.application;

import com.foodify.server.modules.admin.restaurant.dto.AddRestaurantDto;
import com.foodify.server.modules.admin.restaurant.dto.DayScheduleDto;
import com.foodify.server.modules.admin.restaurant.dto.RestaurantDto;
import com.foodify.server.modules.admin.restaurant.repository.AdminRestaurantAdminRepository;
import com.foodify.server.modules.admin.restaurant.repository.AdminRestaurantRepository;
import com.foodify.server.modules.admin.restaurant.repository.RestaurantSpecification;
import com.foodify.server.modules.identity.domain.AuthProvider;
import com.foodify.server.modules.identity.domain.RestaurantAdmin;
import com.foodify.server.modules.identity.domain.Role;
import com.foodify.server.modules.identity.repository.RestaurantAdminRepository;
import com.foodify.server.modules.restaurants.domain.Restaurant;
import com.foodify.server.modules.restaurants.domain.RestaurantWeeklyOperatingHour;
import com.foodify.server.modules.restaurants.dto.NewUserDto;
import com.foodify.server.modules.restaurants.mapper.RestaurantMapper;
import com.foodify.server.modules.restaurants.repository.RestaurantRepository;
import com.foodify.server.modules.storage.application.CloudflareImagesService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.HashSet;

@Service
@RequiredArgsConstructor
public class AdminRestaurantService {
    private final AdminRestaurantRepository restaurantRepository;
    private final RestaurantMapper restaurantMapper;
    private final AdminRestaurantAdminRepository restaurantAdminRepository;
    private final PasswordEncoder passwordEncoder;
    private final CloudflareImagesService cloudflareImagesService;

    public Restaurant addRestaurant(AddRestaurantDto dto, MultipartFile image, MultipartFile icon) throws IOException {
        Restaurant restaurant = this.restaurantMapper.toEntity(dto);
        if (dto.getCategories() != null) {
            restaurant.setCategories(new HashSet<>(dto.getCategories()));
        }
        restaurant.setCommissionRate(resolveCommissionRate(dto.getCommissionRate(), null));

        restaurant.setImageUrl(storeFile(image));

        if (icon != null && !icon.isEmpty()) {
            restaurant.setIconUrl(storeFile(icon));
        }

        NewUserDto adminPayload = dto.getAdmin();

        RestaurantAdmin restaurantAdmin = new RestaurantAdmin();
        restaurantAdmin.setName(adminPayload.getName());
        restaurantAdmin.setEmail(adminPayload.getEmail());
        restaurantAdmin.setPassword(this.passwordEncoder.encode(adminPayload.getPassword()));
        restaurantAdmin.setRole(Role.RESTAURANT_ADMIN);
        restaurantAdmin.setEnabled(true);
        restaurantAdmin.setAuthProvider(AuthProvider.LOCAL);
        restaurant.setAdmin(restaurantAdminRepository.save(restaurantAdmin));

        return this.restaurantRepository.save(restaurant);
    }

    private String storeFile(MultipartFile file) throws IOException {
        return cloudflareImagesService.uploadImage(file);
    }

    private BigDecimal resolveCommissionRate(BigDecimal requestedRate, BigDecimal fallbackRate) {
        BigDecimal defaultCommissionRate = BigDecimal.valueOf(0.17).setScale(4, RoundingMode.HALF_UP);
        BigDecimal minimumCommissionRate = BigDecimal.valueOf(0.12).setScale(4, RoundingMode.HALF_UP);

        BigDecimal resolved = requestedRate != null
                ? requestedRate
                : (fallbackRate != null ? fallbackRate : defaultCommissionRate);

        BigDecimal normalized = resolved.setScale(4, RoundingMode.HALF_UP);
        if (normalized.compareTo(minimumCommissionRate) < 0) {
            normalized = minimumCommissionRate;
        }
        if (normalized.compareTo(BigDecimal.ONE) > 0) {
            normalized = BigDecimal.ONE.setScale(4, RoundingMode.HALF_UP);
        }
        if (normalized.compareTo(BigDecimal.ZERO) < 0) {
            normalized = minimumCommissionRate;
        }

        return normalized;
    }

    public Page<RestaurantDto> searchRestaurants(String query, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Restaurant> restaurants = this.restaurantRepository.findAll(
                RestaurantSpecification.search(query), 
                pageable
        );
        return restaurants.map(this::toRestaurantDto);
    }

    private RestaurantDto toRestaurantDto(Restaurant restaurant) {
        DayOfWeek today = LocalDate.now().getDayOfWeek();
        DayScheduleDto scheduleDto = restaurant.getOperatingHours()
                .stream()
                .filter(hour -> hour.getDayOfWeek() == today)
                .findFirst()
                .map(hour -> DayScheduleDto.builder()
                        .opensAt(hour.getOpensAt())
                        .closesAt(hour.getClosesAt())
                        .open(hour.isOpen())
                        .build()
                )
                .orElse(null);
        
        return RestaurantDto.builder()
                .id(restaurant.getId())
                .name(restaurant.getName())
                .imageUrl(restaurant.getImageUrl())
                .categories(restaurant.getCategories())
                .rating(restaurant.getRating())
                .todaySchedule(scheduleDto)
                .build();
    }
}
