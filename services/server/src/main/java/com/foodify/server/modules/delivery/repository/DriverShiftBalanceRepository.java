package com.foodify.server.modules.delivery.repository;

import com.foodify.server.modules.delivery.domain.DriverShiftBalance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DriverShiftBalanceRepository extends JpaRepository<DriverShiftBalance, Long> {
    Optional<DriverShiftBalance> findByShift_Id(Long shiftId);

    @Query("""
            select coalesce(sum(balance.driverShare), 0)
            from DriverShiftBalance balance
            where balance.shift.driver.id = :driverId
            """)
    BigDecimal sumDriverShareByDriverId(@Param("driverId") Long driverId);

    @Query("""
            select coalesce(sum(balance.driverShare), 0)
            from DriverShiftBalance balance
            where balance.shift.driver.id = :driverId
              and balance.shift.startedAt >= :start
            """)
    BigDecimal sumDriverShareByDriverIdAndStartedAtGreaterThanEqual(
            @Param("driverId") Long driverId,
            @Param("start") LocalDateTime start
    );

    @Query("""
            select coalesce(sum(balance.driverShare), 0)
            from DriverShiftBalance balance
            where balance.shift.driver.id = :driverId
              and balance.shift.startedAt < :end
            """)
    BigDecimal sumDriverShareByDriverIdAndStartedAtLessThan(
            @Param("driverId") Long driverId,
            @Param("end") LocalDateTime end
    );

    @Query("""
            select coalesce(sum(balance.driverShare), 0)
            from DriverShiftBalance balance
            where balance.shift.driver.id = :driverId
              and balance.shift.startedAt >= :start
              and balance.shift.startedAt < :end
            """)
    BigDecimal sumDriverShareByDriverIdAndStartedAtBetween(
            @Param("driverId") Long driverId,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );
}
