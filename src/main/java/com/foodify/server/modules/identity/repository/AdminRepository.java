package com.foodify.server.modules.identity.repository;

import com.foodify.server.modules.identity.domain.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Admin, Long> {
}
