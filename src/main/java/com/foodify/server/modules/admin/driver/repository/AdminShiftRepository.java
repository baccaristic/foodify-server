package com.foodify.server.modules.admin.driver.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.foodify.server.modules.delivery.domain.DriverShift;

import java.time.LocalDateTime;

public interface AdminShiftRepository extends JpaRepository<DriverShift, Long> {

    @EntityGraph(attributePaths = {
            "driver",
            "balance",
            "deliveries",
            "deliveries.order",
            "deliveries.order.restaurant",
            "deliveries.order.savedAddress",
            "deliveries.rating"
    })
    @Query("""
            SELECT DISTINCT shift
            FROM DriverShift shift
            WHERE shift.driver.id = :driverId 
                AND CAST(shift.startedAt AS LocalDate) = CAST(:date AS LocalDate)
            ORDER BY shift.startedAt DESC
            """)
    Page<DriverShift> findShiftsWithFilters(
            @Param("driverId") Long driverId,
            @Param("date") LocalDateTime date,
            Pageable pageable
    );
}