package com.foodify.server.modules.rewards.repository;

import com.foodify.server.modules.orders.domain.Order;
import com.foodify.server.modules.rewards.domain.Coupon;
import com.foodify.server.modules.rewards.domain.CouponRedemption;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CouponRedemptionRepository extends JpaRepository<CouponRedemption, Long> {
    boolean existsByCouponAndClient_Id(Coupon coupon, Long clientId);

    Optional<CouponRedemption> findByCouponAndClient_Id(Coupon coupon, Long clientId);

    Optional<CouponRedemption> findByOrder(Order order);
}
