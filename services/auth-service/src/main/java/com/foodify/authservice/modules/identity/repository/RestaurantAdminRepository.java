package com.foodify.authservice.modules.identity.repository;

import com.foodify.authservice.modules.identity.domain.RestaurantAdmin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantAdminRepository extends JpaRepository<RestaurantAdmin, Long> {
}
