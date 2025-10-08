package com.foodify.catalogservice.repository;

import com.foodify.server.modules.restaurants.domain.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {
}
