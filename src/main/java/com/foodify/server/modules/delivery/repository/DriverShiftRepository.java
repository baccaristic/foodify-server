package com.foodify.server.modules.delivery.repository;

import com.foodify.server.modules.delivery.domain.DriverShift;
import com.foodify.server.modules.delivery.domain.DriverShiftStatus;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface DriverShiftRepository extends JpaRepository<DriverShift, Long> {
    Optional<DriverShift> findTopByDriverIdAndStatusOrderByStartedAtDesc(Long driverId, DriverShiftStatus status);

    @EntityGraph(attributePaths = "balance")
    @Query("""
            select shift
            from DriverShift shift
            where shift.driver.id = :driverId
              and shift.balance is not null
            order by shift.startedAt desc
            """)
    List<DriverShift> findAllWithBalanceByDriverIdOrderByStartedAtDesc(@Param("driverId") Long driverId);

    @EntityGraph(attributePaths = "balance")
    @Query("""
            select shift
            from DriverShift shift
            where shift.driver.id = :driverId
              and shift.balance is not null
              and shift.startedAt >= :start
            order by shift.startedAt desc
            """)
    List<DriverShift> findAllWithBalanceByDriverIdAndStartedAtGreaterThanEqualOrderByStartedAtDesc(
            @Param("driverId") Long driverId,
            @Param("start") LocalDateTime start
    );

    @EntityGraph(attributePaths = "balance")
    @Query("""
            select shift
            from DriverShift shift
            where shift.driver.id = :driverId
              and shift.balance is not null
              and shift.startedAt < :end
            order by shift.startedAt desc
            """)
    List<DriverShift> findAllWithBalanceByDriverIdAndStartedAtLessThanOrderByStartedAtDesc(
            @Param("driverId") Long driverId,
            @Param("end") LocalDateTime end
    );

    @EntityGraph(attributePaths = "balance")
    @Query("""
            select shift
            from DriverShift shift
            where shift.driver.id = :driverId
              and shift.balance is not null
              and shift.startedAt >= :start
              and shift.startedAt < :end
            order by shift.startedAt desc
            """)
    List<DriverShift> findAllWithBalanceByDriverIdAndStartedAtBetweenOrderByStartedAtDesc(
            @Param("driverId") Long driverId,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );

    @EntityGraph(attributePaths = {
            "balance",
            "deliveries",
            "deliveries.order",
            "deliveries.order.restaurant",
            "deliveries.order.savedAddress"
    })
    @Query("""
            select shift
            from DriverShift shift
            where shift.id = :shiftId
              and shift.driver.id = :driverId
            """)
    Optional<DriverShift> findByIdAndDriverIdWithDetails(
            @Param("shiftId") Long shiftId,
            @Param("driverId") Long driverId
    );
}
