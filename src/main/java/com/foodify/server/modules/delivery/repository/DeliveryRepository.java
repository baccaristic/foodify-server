package com.foodify.server.modules.delivery.repository;

import com.foodify.server.modules.delivery.domain.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DeliveryRepository extends JpaRepository<Delivery, Long> {
    Optional<Delivery> findTopByDriverIdOrderByDeliveredTimeDesc(Long driverId);
}
