package com.foodify.server.modules.identity.repository;

import com.foodify.server.modules.identity.domain.RestaurantCashier;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantCashierRepository extends JpaRepository<RestaurantCashier, Long> {
}
