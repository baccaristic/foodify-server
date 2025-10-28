package com.foodify.server.modules.identity.repository;

import com.foodify.server.modules.identity.domain.Driver;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DriverRepository extends JpaRepository<Driver, Long> {
    long countByAvailableTrue();
}
