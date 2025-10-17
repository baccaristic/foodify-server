package com.foodify.server.modules.delivery.repository;

import com.foodify.server.modules.delivery.domain.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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
}
