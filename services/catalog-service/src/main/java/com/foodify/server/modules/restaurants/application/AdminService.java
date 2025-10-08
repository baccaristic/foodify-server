package com.foodify.server.modules.restaurants.application;

import com.foodify.server.modules.restaurants.dto.RestaurantDto;
import com.foodify.server.modules.identity.domain.Role;
import com.foodify.server.modules.restaurants.mapper.RestaurantMapper;
import com.foodify.server.modules.identity.domain.Driver;
import com.foodify.server.modules.restaurants.domain.Restaurant;
import com.foodify.server.modules.identity.domain.RestaurantAdmin;
import com.foodify.server.modules.identity.repository.RestaurantAdminRepository;
import com.foodify.server.modules.restaurants.repository.RestaurantRepository;
import com.foodify.server.modules.identity.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final UserRepository userRepository;
    private final RestaurantRepository  restaurantRepository;
    private final RestaurantMapper restaurantMapper;
    private final RestaurantAdminRepository restaurantAdminRepository;
    private final PasswordEncoder passwordEncoder;

    public Driver addDriver(Driver driver) {
        return null;
    }

    public Restaurant addRestaurant(RestaurantDto dto, MultipartFile file) throws IOException {
        Restaurant restaurant = this.restaurantMapper.toEntity(dto);

        String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path path = Paths.get("uploads").resolve(filename);
        Files.createDirectories(path.getParent());
        Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

        restaurant.setImageUrl(filename);

        RestaurantAdmin restaurantAdmin = new RestaurantAdmin();
        restaurantAdmin.setName(dto.getAdmin().getName());
        restaurantAdmin.setEmail(dto.getAdmin().getEmail());
        restaurantAdmin.setPassword(this.passwordEncoder.encode(dto.getAdmin().getPassword()));
        restaurantAdmin.setRole(Role.RESTAURANT_ADMIN);
        restaurantAdmin.setEnabled(true);
        restaurant.setAdmin(restaurantAdminRepository.save(restaurantAdmin));
        return this.restaurantRepository.save(restaurant);
    }
}
