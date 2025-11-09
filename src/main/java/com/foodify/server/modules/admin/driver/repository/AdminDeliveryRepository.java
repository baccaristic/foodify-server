package com.foodify.server.modules.admin.driver.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.foodify.server.modules.admin.driver.dto.DailyOnTimePercentageDto;
import com.foodify.server.modules.admin.driver.dto.DailyRatingDto;
import com.foodify.server.modules.delivery.domain.Delivery;

import io.lettuce.core.dynamic.annotation.Param;

public interface AdminDeliveryRepository extends JpaRepository<Delivery, Long> {
    @Query("""
            SELECT COALESCE(AVG(r.overallRating), 0.0)
            FROM Delivery del
            LEFT JOIN del.rating r
            WHERE del.driver.id = :driverId
                AND del.deliveredTime BETWEEN :startDate AND :endDate
            """)
    Double getDriverRating(
            @Param("driverId") Long driverId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    @Query("""
            SELECT
                (SUM(
                    CASE
                        WHEN (EXTRACT(EPOCH FROM d.deliveredTime) - EXTRACT(EPOCH FROM d.assignedTime))
                            <= COALESCE(d.deliveryTime, 0) + COALESCE(d.timeToPickUp, 0)
                        THEN 1
                        ELSE 0
                    END
                ) * 100.0 / COUNT(d.id))
            FROM Delivery d
            WHERE d.driver.id = :driverId
                AND d.deliveredTime BETWEEN :startDate AND :endDate
            """)
    Double getDriverOnTimePercentage(
            @Param("driverId") Long driverId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    @Query("""
            SELECT AVG(EXTRACT(EPOCH FROM d.deliveredTime) - EXTRACT(EPOCH FROM d.assignedTime))/60
            FROM Delivery d
            WHERE d.driver.id = :driverId
                AND d.deliveredTime BETWEEN :startDate AND :endDate
            """)
    Double getDriverAverageDeliveryTime(
            @Param("driverId") Long driverId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    @Query("""
            SELECT new com.foodify.server.modules.admin.driver.dto.DailyRatingDto(
                CAST(FUNCTION('DATE', r.createdAt) AS LocalDate),
                ROUND(AVG(r.overallRating), 2)
            )
            FROM Delivery d
            JOIN d.rating r
            WHERE d.driver.id = :driverId
                AND r.createdAt BETWEEN :startDate AND :endDate
            GROUP BY FUNCTION('DATE', r.createdAt)
            ORDER BY FUNCTION('DATE', r.createdAt)
            """)
    List<DailyRatingDto> getDriverAverageRatingByDay(
            @Param("driverId") Long driverId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    @Query("""
            SELECT new com.foodify.server.modules.admin.driver.dto.DailyOnTimePercentageDto(
                CAST(FUNCTION('DATE', d.deliveredTime) AS LocalDate),
                ROUND(
                    (SUM(
                        CASE
                            WHEN (EXTRACT(EPOCH FROM d.deliveredTime) - EXTRACT(EPOCH FROM d.assignedTime))
                                 <= COALESCE(d.deliveryTime, 0) + COALESCE(d.timeToPickUp, 0)
                            THEN 1
                            ELSE 0
                        END
                    ) * 100.0 / COUNT(d.id)),
                    2
                )
            )
            FROM Delivery d
            WHERE d.driver.id = :driverId
                AND d.deliveredTime BETWEEN :startDate AND :endDate
            GROUP BY FUNCTION('DATE', d.deliveredTime)
            ORDER BY FUNCTION('DATE', d.deliveredTime)
            """)
    List<DailyOnTimePercentageDto> getDriverOnTimePercentageByDay(
            @Param("driverId") Long driverId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

}
