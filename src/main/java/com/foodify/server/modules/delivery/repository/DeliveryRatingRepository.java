package com.foodify.server.modules.delivery.repository;

import com.foodify.server.modules.delivery.domain.DeliveryRating;
import com.foodify.server.modules.delivery.dto.DriverRatingSummaryDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface DeliveryRatingRepository extends JpaRepository<DeliveryRating, Long> {

    @EntityGraph(attributePaths = {"delivery", "delivery.order", "delivery.driver"})
    Optional<DeliveryRating> findByDelivery_Order_Id(Long orderId);

    @EntityGraph(attributePaths = {"delivery", "delivery.order", "delivery.driver"})
    Page<DeliveryRating> findByDelivery_Driver_IdOrderByCreatedAtDesc(Long driverId, Pageable pageable);

    @Query("""
            select new com.foodify.server.modules.delivery.dto.DriverRatingSummaryDto(
                driver.id,
                count(rating),
                coalesce(avg(rating.timingRating), 0.0),
                coalesce(avg(rating.foodConditionRating), 0.0),
                coalesce(avg(rating.professionalismRating), 0.0),
                coalesce(avg(rating.overallRating), 0.0)
            )
            from DeliveryRating rating
            join rating.delivery delivery
            join delivery.driver driver
            where driver.id = :driverId
            group by driver.id
            """)
    Optional<DriverRatingSummaryDto> findSummaryByDriverId(@Param("driverId") Long driverId);
}
