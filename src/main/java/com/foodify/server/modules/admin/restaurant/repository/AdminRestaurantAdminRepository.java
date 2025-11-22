package com.foodify.server.modules.admin.restaurant.repository;

import com.foodify.server.modules.identity.domain.RestaurantAdmin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRestaurantAdminRepository extends JpaRepository<RestaurantAdmin, Long> {
}
