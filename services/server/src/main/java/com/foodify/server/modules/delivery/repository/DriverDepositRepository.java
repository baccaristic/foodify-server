package com.foodify.server.modules.delivery.repository;

import com.foodify.server.modules.delivery.domain.DriverDeposit;
import com.foodify.server.modules.delivery.domain.DriverDepositStatus;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DriverDepositRepository extends JpaRepository<DriverDeposit, Long> {
    @EntityGraph(attributePaths = "driver")
    List<DriverDeposit> findByDriver_IdOrderByCreatedAtDesc(Long driverId);

    @EntityGraph(attributePaths = "driver")
    List<DriverDeposit> findAllByOrderByCreatedAtDesc();

    @EntityGraph(attributePaths = "driver")
    List<DriverDeposit> findAllByStatusOrderByCreatedAtDesc(DriverDepositStatus status);

    Optional<DriverDeposit> findFirstByDriver_IdAndStatusOrderByCreatedAtDesc(Long driverId, DriverDepositStatus status);
}
