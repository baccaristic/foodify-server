package com.foodify.server.modules.payments.points.repository;

import com.foodify.server.modules.payments.points.domain.PointsPayment;
import com.foodify.server.modules.payments.points.domain.PointsPaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PointsPaymentRepository extends JpaRepository<PointsPayment, Long> {
    
    Optional<PointsPayment> findByPaymentToken(String paymentToken);
    
    List<PointsPayment> findByRestaurant_IdOrderByCreatedAtDesc(Long restaurantId);
    
    List<PointsPayment> findByClient_IdOrderByCreatedAtDesc(Long clientId);
    
    List<PointsPayment> findByStatusOrderByCreatedAtDesc(PointsPaymentStatus status);
}
