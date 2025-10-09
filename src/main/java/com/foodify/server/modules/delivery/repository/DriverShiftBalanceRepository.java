package com.foodify.server.modules.delivery.repository;

import com.foodify.server.modules.delivery.domain.DriverShiftBalance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DriverShiftBalanceRepository extends JpaRepository<DriverShiftBalance, Long> {
    Optional<DriverShiftBalance> findByShift_Id(Long shiftId);
}
