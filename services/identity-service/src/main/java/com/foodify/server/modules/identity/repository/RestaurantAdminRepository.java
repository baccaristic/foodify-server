package com.foodify.server.modules.identity.repository;

import com.foodify.server.modules.identity.domain.RestaurantAdmin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantAdminRepository extends JpaRepository<RestaurantAdmin, Long> {
}
