package com.foodify.server.modules.delivery.repository;

import com.foodify.server.modules.delivery.domain.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface DeliveryRepository extends JpaRepository<Delivery, Long> {
    Optional<Delivery> findTopByDriverIdOrderByDeliveredTimeDesc(Long driverId);

    @Query("""
            select delivery
            from Delivery delivery
            where delivery.driver.id in :driverIds
            order by delivery.driver.id asc,
                     delivery.deliveredTime desc,
                     delivery.pickupTime desc,
                     delivery.assignedTime desc
            """)
    List<Delivery> findRecentForDrivers(@Param("driverIds") Collection<Long> driverIds);
    
    @Query("""
            SELECT COUNT(d) FROM Delivery d 
            WHERE d.driver.id = :driverId 
            AND d.deliveredTime IS NOT NULL
            """)
    long countCompletedByDriverId(@Param("driverId") Long driverId);
    
    @Query("""
            SELECT COUNT(d) FROM Delivery d 
            JOIN d.order o
            WHERE d.driver.id = :driverId 
            AND d.deliveredTime IS NOT NULL
            AND d.deliveryTime <= 30
            """)
    long countOnTimeByDriverId(@Param("driverId") Long driverId);
    
    @Query("""
            SELECT AVG(d.deliveryTime) FROM Delivery d 
            WHERE d.driver.id = :driverId 
            AND d.deliveredTime IS NOT NULL
            """)
    Double getAverageDeliveryTimeByDriverId(@Param("driverId") Long driverId);
    
    @Query("""
            SELECT d FROM Delivery d
            JOIN FETCH d.shift s
            WHERE s.driver.id = :driverId
            AND s.startedAt >= :startDate AND s.startedAt < :endDate
            ORDER BY s.startedAt DESC
            """)
    List<Delivery> findByDriverIdAndDateRange(
            @Param("driverId") Long driverId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );
}
