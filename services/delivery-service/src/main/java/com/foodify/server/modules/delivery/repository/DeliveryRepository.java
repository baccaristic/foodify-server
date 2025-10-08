package com.foodify.server.modules.delivery.repository;

import com.foodify.server.modules.delivery.domain.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliveryRepository extends JpaRepository<Delivery, Long> {
}
