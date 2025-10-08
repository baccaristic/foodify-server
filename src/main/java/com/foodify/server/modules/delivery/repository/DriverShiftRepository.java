package com.foodify.server.modules.delivery.repository;

import com.foodify.server.modules.delivery.domain.DriverShift;
import com.foodify.server.modules.delivery.domain.DriverShiftStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DriverShiftRepository extends JpaRepository<DriverShift, Long> {
    Optional<DriverShift> findTopByDriverIdAndStatusOrderByStartedAtDesc(Long driverId, DriverShiftStatus status);
}
