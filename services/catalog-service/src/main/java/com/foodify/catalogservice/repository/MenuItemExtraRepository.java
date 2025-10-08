package com.foodify.catalogservice.repository;

import com.foodify.server.modules.restaurants.domain.MenuItemExtra;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuItemExtraRepository extends JpaRepository<MenuItemExtra, Long> {
}
