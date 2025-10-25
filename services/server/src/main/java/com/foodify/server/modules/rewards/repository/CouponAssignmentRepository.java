package com.foodify.server.modules.rewards.repository;

import com.foodify.server.modules.identity.domain.Client;
import com.foodify.server.modules.rewards.domain.Coupon;
import com.foodify.server.modules.rewards.domain.CouponAssignment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CouponAssignmentRepository extends JpaRepository<CouponAssignment, Long> {
    Optional<CouponAssignment> findByCouponAndClient(Coupon coupon, Client client);

    List<CouponAssignment> findByClient_Id(Long clientId);
}
