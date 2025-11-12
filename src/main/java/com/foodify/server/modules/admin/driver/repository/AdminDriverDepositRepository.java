package com.foodify.server.modules.admin.driver.repository;

import com.foodify.server.modules.admin.driver.dto.DailyEarningDto;
import com.foodify.server.modules.admin.driver.dto.DailyRatingDto;
import com.foodify.server.modules.admin.driver.dto.DriverRatingCommentDto;
import com.foodify.server.modules.admin.driver.dto.RatingDistributionDto;
import com.foodify.server.modules.delivery.domain.DeliveryRating;
import com.foodify.server.modules.delivery.domain.DriverDeposit;
import com.foodify.server.modules.delivery.domain.DriverDepositStatus;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface AdminDriverDepositRepository extends JpaRepository<DriverDeposit, Long> {

    @Query("""
            SELECT new com.foodify.server.modules.admin.driver.dto.DailyEarningDto(
                CAST(FUNCTION('DATE', d.confirmedAt) AS LocalDate),
                CAST(SUM(d.earningsPaid) AS Double)
            )
            FROM DriverDeposit d
            WHERE d.driver.id = :driverId
                AND (CAST(:startDate AS timestamp) IS NULL OR d.confirmedAt >= :startDate)
                AND (CAST(:endDate AS timestamp) IS NULL OR d.confirmedAt <= :endDate)
            GROUP BY FUNCTION('DATE', d.confirmedAt)
            ORDER BY FUNCTION('DATE', d.confirmedAt)
            """)
    List<DailyEarningDto> getDriverEarningByDay(
            @org.springframework.data.repository.query.Param("driverId") Long driverId,
            @org.springframework.data.repository.query.Param("startDate") LocalDateTime startDate,
            @org.springframework.data.repository.query.Param("endDate") LocalDateTime endDate);

    @EntityGraph(attributePaths = "driver")
    Page<DriverDeposit> findAllByDriverIdOrderByCreatedAtDesc(Long driverId, Pageable pageable);

    @EntityGraph(attributePaths = "driver")
    Page<DriverDeposit> findAllByDriverIdAndStatusOrderByCreatedAtDesc(
            Long driverId,
            DriverDepositStatus status,
            Pageable pageable
    );
}
