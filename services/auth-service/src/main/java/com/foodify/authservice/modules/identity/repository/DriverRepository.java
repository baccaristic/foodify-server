package com.foodify.authservice.modules.identity.repository;

import com.foodify.authservice.modules.identity.domain.Driver;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DriverRepository extends JpaRepository<Driver, Long> {
}
