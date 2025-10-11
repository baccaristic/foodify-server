package com.foodify.server.modules.delivery.repository;

import com.foodify.server.modules.delivery.domain.DriverShift;
import com.foodify.server.modules.delivery.domain.DriverShiftStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface DriverShiftRepository extends JpaRepository<DriverShift, Long> {
    Optional<DriverShift> findTopByDriverIdAndStatusOrderByStartedAtDesc(Long driverId, DriverShiftStatus status);

    @Query("""
            select shift
            from DriverShift shift
            left join fetch shift.balance balance
            where shift.driver.id = :driverId
              and balance is not null
              and (:start is null or shift.startedAt >= :start)
              and (:end is null or shift.startedAt < :end)
            order by shift.startedAt desc
            """)
    List<DriverShift> findAllWithBalanceByDriverIdAndStartedAtBetween(
            @Param("driverId") Long driverId,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );
}
