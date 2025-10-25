package com.foodify.authservice.modules.identity.repository;

import com.foodify.authservice.modules.identity.domain.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Admin, Long> {
}
