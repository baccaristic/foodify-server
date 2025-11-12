package com.foodify.server.modules.admin.driver.repository;

import java.time.LocalDateTime;
import java.util.List;

import com.foodify.server.modules.admin.driver.dto.DailyRatingDto;
import com.foodify.server.modules.admin.driver.dto.RatingDistributionDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.foodify.server.modules.admin.driver.dto.DriverRatingCommentDto;
import com.foodify.server.modules.delivery.domain.DeliveryRating;
import io.lettuce.core.dynamic.annotation.Param;

public interface AdminDeliveryRatingRepository extends JpaRepository<DeliveryRating, Long> {

    @Query("""
            SELECT new com.foodify.server.modules.admin.driver.dto.DriverRatingCommentDto(
                r.delivery.id,
                r.delivery.order.id,
                r.delivery.order.client.name,
                r.timingRating,
                r.foodConditionRating,
                r.professionalismRating,
                r.overallRating,
                r.comments,
                r.createdAt
            )
            FROM DeliveryRating r
            WHERE r.delivery.driver.id = :driverId
                AND r.comments IS NOT NULL
                AND r.comments <> ''
                AND (CAST(:startDate AS timestamp) IS NULL OR r.createdAt >= :startDate)
                AND (CAST(:endDate AS timestamp) IS NULL OR r.createdAt <= :endDate)
            ORDER BY r.createdAt DESC
            """)
    Page<DriverRatingCommentDto> findRatingsWithCommentsByDriverId(
            @Param("driverId") Long driverId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            Pageable pageable);


    @Query("""
            SELECT new com.foodify.server.modules.admin.driver.dto.RatingDistributionDto(
                CAST(ROUND(r.overallRating) AS integer),
                COUNT(r)
            )
            FROM DeliveryRating r
            WHERE r.delivery.driver.id = :driverId
                AND (CAST(:startDate AS timestamp) IS NULL OR r.createdAt >= :startDate)
                AND (CAST(:endDate AS timestamp) IS NULL OR r.createdAt <= :endDate)
            GROUP BY ROUND(r.overallRating)
            ORDER BY ROUND(r.overallRating)
            """)
    List<RatingDistributionDto> getDriverRatingDistribution(
            @Param("driverId") Long driverId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    @Query("""
            SELECT new com.foodify.server.modules.admin.driver.dto.DailyRatingDto(
                CAST(FUNCTION('DATE', r.createdAt) AS LocalDate),
                ROUND(AVG(r.overallRating), 2)
            )
            FROM DeliveryRating r
            WHERE r.delivery.driver.id = :driverId
                AND (CAST(:startDate AS timestamp) IS NULL OR r.createdAt >= :startDate)
                AND (CAST(:endDate AS timestamp) IS NULL OR r.createdAt <= :endDate)
            GROUP BY FUNCTION('DATE', r.createdAt)
            ORDER BY FUNCTION('DATE', r.createdAt)
            """)
    List<DailyRatingDto> getDriverAverageRatingByDay(
            @org.springframework.data.repository.query.Param("driverId") Long driverId,
            @org.springframework.data.repository.query.Param("startDate") LocalDateTime startDate,
            @org.springframework.data.repository.query.Param("endDate") LocalDateTime endDate);
}
