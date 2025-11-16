package com.foodify.server.modules.admin.driver.repository;

import com.foodify.server.modules.admin.driver.dto.DriverDetailsDto;
import com.foodify.server.modules.identity.domain.Admin;
import com.foodify.server.modules.identity.domain.Driver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AdminAdminRepository extends JpaRepository<Admin, Long>, JpaSpecificationExecutor<Driver> {
}
