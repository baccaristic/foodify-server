package com.foodify.server.modules.rewards.application;

import com.foodify.server.modules.identity.domain.Client;
import com.foodify.server.modules.orders.domain.Order;
import com.foodify.server.modules.rewards.domain.Coupon;
import com.foodify.server.modules.rewards.domain.CouponAssignment;
import com.foodify.server.modules.rewards.domain.CouponRedemption;
import com.foodify.server.modules.rewards.domain.CouponType;
import com.foodify.server.modules.rewards.dto.CouponDto;
import com.foodify.server.modules.rewards.repository.CouponAssignmentRepository;
import com.foodify.server.modules.rewards.repository.CouponRedemptionRepository;
import com.foodify.server.modules.rewards.repository.CouponRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Transactional
public class CouponService {

    private final CouponRepository couponRepository;
    private final CouponAssignmentRepository assignmentRepository;
    private final CouponRedemptionRepository redemptionRepository;

    public CouponService(CouponRepository couponRepository,
                         CouponAssignmentRepository assignmentRepository,
                         CouponRedemptionRepository redemptionRepository) {
        this.couponRepository = couponRepository;
        this.assignmentRepository = assignmentRepository;
        this.redemptionRepository = redemptionRepository;
    }

    @Transactional(Transactional.TxType.SUPPORTS)
    public CouponApplicationResult previewApplication(Order order, Client client, String couponCode) {
        if (order == null) {
            throw new IllegalArgumentException("Order is required to preview coupon application");
        }
        if (client == null) {
            throw new IllegalArgumentException("Client is required to preview coupon application");
        }
        if (couponCode == null || couponCode.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Coupon code is required");
        }

        Coupon coupon = couponRepository.findByCodeIgnoreCase(couponCode.trim())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Coupon not found"));

        if (!coupon.isActive()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Coupon is inactive");
        }

        if (!coupon.isPublicCoupon()) {
            assignmentRepository.findByCouponAndClient(coupon, client)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN, "Coupon is not assigned to this user"));
        }

        if (redemptionRepository.existsByCouponAndClient_Id(coupon, client.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Coupon already redeemed");
        }

        BigDecimal itemsTotal = Optional.ofNullable(order.getItemsTotal()).orElse(BigDecimal.ZERO);
        BigDecimal deliveryFee = Optional.ofNullable(order.getDeliveryFee()).orElse(BigDecimal.ZERO);
        BigDecimal baseTotal = itemsTotal.add(deliveryFee).setScale(2, RoundingMode.HALF_UP);

        if (baseTotal.compareTo(BigDecimal.ZERO) <= 0) {
            return new CouponApplicationResult(coupon, BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP), null);
        }

        BigDecimal discount;
        BigDecimal deliveryFeeOverride = null;

        if (coupon.getType() == CouponType.PERCENTAGE_DISCOUNT) {
            BigDecimal percent = Optional.ofNullable(coupon.getDiscountPercent())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Percentage coupon is misconfigured"));
            if (percent.compareTo(BigDecimal.ZERO) <= 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Percentage coupon must be greater than zero");
            }
            discount = baseTotal.multiply(percent)
                    .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        } else if (coupon.getType() == CouponType.FREE_DELIVERY) {
            discount = deliveryFee.setScale(2, RoundingMode.HALF_UP);
            deliveryFeeOverride = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unsupported coupon type");
        }

        if (discount.compareTo(baseTotal) > 0) {
            discount = baseTotal;
        }

        return new CouponApplicationResult(
                coupon,
                discount.setScale(2, RoundingMode.HALF_UP),
                deliveryFeeOverride
        );
    }

    public void recordRedemption(Coupon coupon, Client client, Order order) {
        if (coupon == null || client == null) {
            return;
        }

        if (redemptionRepository.existsByCouponAndClient_Id(coupon, client.getId())) {
            if (order != null) {
                redemptionRepository.findByCouponAndClient_Id(coupon, client.getId()).ifPresent(existing -> {
                    if (existing.getOrder() == null && !Objects.equals(existing.getOrder(), order)) {
                        existing.setOrder(order);
                        existing.setRedeemedAt(LocalDateTime.now());
                        redemptionRepository.save(existing);
                    }
                });
            }
            return;
        }

        CouponRedemption redemption = new CouponRedemption();
        redemption.setCoupon(coupon);
        redemption.setClient(client);
        redemption.setOrder(order);
        redemptionRepository.save(redemption);
    }

    @Transactional(Transactional.TxType.SUPPORTS)
    public List<CouponDto> getAvailableCoupons(Long clientId) {
        List<CouponDto> coupons = new ArrayList<>();
        if (clientId == null) {
            return coupons;
        }

        couponRepository.findByPublicCouponTrueAndActiveTrue().forEach(coupon -> {
            boolean redeemed = redemptionRepository.existsByCouponAndClient_Id(coupon, clientId);
            coupons.add(new CouponDto(
                    coupon.getCode(),
                    coupon.getType(),
                    coupon.getDiscountPercent(),
                    true,
                    redeemed,
                    coupon.isActive(),
                    coupon.isCreatedFromPoints(),
                    null
            ));
        });

        assignmentRepository.findByClient_Id(clientId).forEach(assignment -> {
            Coupon coupon = assignment.getCoupon();
            if (coupon == null || !coupon.isActive()) {
                return;
            }
            Long assignedClientId = Optional.ofNullable(assignment.getClient())
                    .map(Client::getId)
                    .orElse(clientId);
            boolean redeemed = redemptionRepository.existsByCouponAndClient_Id(coupon, assignedClientId);
            coupons.add(new CouponDto(
                    coupon.getCode(),
                    coupon.getType(),
                    coupon.getDiscountPercent(),
                    coupon.isPublicCoupon(),
                    redeemed,
                    coupon.isActive(),
                    coupon.isCreatedFromPoints(),
                    assignment.getAssignedAt()
            ));
        });

        return coupons;
    }
}
