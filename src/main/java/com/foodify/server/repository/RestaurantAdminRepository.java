package com.foodify.server.repository;

import com.foodify.server.models.RestaurantAdmin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantAdminRepository extends JpaRepository<RestaurantAdmin, Long> {
}
