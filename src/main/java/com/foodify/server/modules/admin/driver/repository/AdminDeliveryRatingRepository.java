package com.foodify.server.modules.admin.driver.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.foodify.server.modules.delivery.domain.DeliveryRating;
import io.lettuce.core.dynamic.annotation.Param;

public interface AdminDeliveryRatingRepository extends JpaRepository<DeliveryRating, Long> {

    @EntityGraph(attributePaths = { "delivery", "delivery.order", "delivery.driver", "delivery.order.client" })
    @Query("""
            SELECT r
            FROM DeliveryRating r
            WHERE r.delivery.driver.id = :driverId
                AND r.comments IS NOT NULL
                AND r.comments <> ''
                AND (:startDate IS NULL OR r.createdAt >= :startDate)
                AND (:endDate IS NULL OR r.createdAt <= :endDate)
            ORDER BY r.createdAt DESC
            """)
    Page<DeliveryRating> findRatingsWithCommentsByDriverId(
            @Param("driverId") Long driverId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            Pageable pageable);

    @Query(value = """
            SELECT
                COALESCE(SUM(CASE WHEN ROUND(dr.overall_rating) = stars.ratingLevel THEN 1 ELSE 0 END), 0) AS count
            FROM delivery_ratings dr
            INNER JOIN delivery d ON dr.delivery_id = d.id
            CROSS JOIN (VALUES (1), (2), (3), (4), (5)) AS stars(ratingLevel)
            WHERE d.driver_id = :driverId
                AND (:startDate IS NULL OR dr.created_at >= :startDate)
                AND (:endDate IS NULL OR dr.created_at <= :endDate)
            GROUP BY stars.ratingLevel
            ORDER BY stars.ratingLevel
            """, nativeQuery = true)
    List<Long> getDriverRatingDistribution(
            @Param("driverId") Long driverId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);
}
