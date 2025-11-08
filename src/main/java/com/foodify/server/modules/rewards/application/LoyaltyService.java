package com.foodify.server.modules.rewards.application;

import com.foodify.server.modules.identity.domain.Client;
import com.foodify.server.modules.identity.repository.ClientRepository;
import com.foodify.server.modules.orders.domain.Order;
import com.foodify.server.modules.rewards.domain.Coupon;
import com.foodify.server.modules.rewards.domain.CouponAssignment;
import com.foodify.server.modules.rewards.domain.CouponType;
import com.foodify.server.modules.rewards.domain.LoyaltyPointTransaction;
import com.foodify.server.modules.rewards.domain.LoyaltyPointTransactionType;
import com.foodify.server.modules.rewards.dto.*;
import com.foodify.server.modules.rewards.repository.CouponAssignmentRepository;
import com.foodify.server.modules.rewards.repository.CouponRepository;
import com.foodify.server.modules.rewards.repository.LoyaltyPointTransactionRepository;
import jakarta.transaction.Transactional;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class LoyaltyService {

    private static final BigDecimal POINTS_EARN_RATE = BigDecimal.valueOf(0.10);
    private static final BigDecimal FREE_DELIVERY_COUPON_COST = BigDecimal.valueOf(250);
    private static final BigDecimal PERCENT_COUPON_COST_PER_PERCENT = BigDecimal.valueOf(15);
    private static final BigDecimal MIN_PERCENT_COUPON = BigDecimal.valueOf(5);
    private static final BigDecimal MAX_PERCENT_COUPON = BigDecimal.valueOf(50);

    private final ClientRepository clientRepository;
    private final LoyaltyPointTransactionRepository transactionRepository;
    private final CouponRepository couponRepository;
    private final CouponAssignmentRepository assignmentRepository;

    public LoyaltyService(ClientRepository clientRepository,
                          LoyaltyPointTransactionRepository transactionRepository,
                          CouponRepository couponRepository,
                          CouponAssignmentRepository assignmentRepository) {
        this.clientRepository = clientRepository;
        this.transactionRepository = transactionRepository;
        this.couponRepository = couponRepository;
        this.assignmentRepository = assignmentRepository;
    }

    public BigDecimal awardPointsForDeliveredOrder(Order order) {
        if (order == null || order.getClient() == null) {
            return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        }

        if (order.getTotal() == null) {
            return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        }

        if (order.getLoyaltyPointsEarned() != null && order.getLoyaltyPointsEarned().compareTo(BigDecimal.ZERO) > 0) {
            return order.getLoyaltyPointsEarned();
        }

        Client client = order.getClient();
        Client managedClient = clientRepository.findById(client.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Client not found"));

        BigDecimal pointsEarned = order.getTotal()
                .multiply(POINTS_EARN_RATE)
                .setScale(2, RoundingMode.HALF_UP);

        if (pointsEarned.compareTo(BigDecimal.ZERO) <= 0) {
            return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        }

        BigDecimal newBalance = Optional.ofNullable(managedClient.getLoyaltyPointsBalance())
                .orElse(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP))
                .add(pointsEarned)
                .setScale(2, RoundingMode.HALF_UP);
        managedClient.setLoyaltyPointsBalance(newBalance);

        LoyaltyPointTransaction transaction = new LoyaltyPointTransaction();
        transaction.setClient(managedClient);
        transaction.setOrder(order);
        transaction.setType(LoyaltyPointTransactionType.EARNED);
        transaction.setPoints(pointsEarned);
        transaction.setDescription("Points earned for order " + order.getId());
        transactionRepository.save(transaction);

        order.setLoyaltyPointsEarned(pointsEarned);

        return pointsEarned;
    }

    @Transactional(Transactional.TxType.SUPPORTS)
    public LoyaltyBalanceResponse getBalance(Long clientId) {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Client not found"));

        List<LoyaltyPointTransaction> transactions = transactionRepository.findByClient_IdOrderByCreatedAtDesc(clientId);

        BigDecimal earned = transactions.stream()
                .filter(t -> t.getType() == LoyaltyPointTransactionType.EARNED)
                .map(LoyaltyPointTransaction::getPoints)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);

        BigDecimal redeemed = transactions.stream()
                .filter(t -> t.getType() == LoyaltyPointTransactionType.REDEEMED)
                .map(LoyaltyPointTransaction::getPoints)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .abs()
                .setScale(2, RoundingMode.HALF_UP);

        BigDecimal balance = Optional.ofNullable(client.getLoyaltyPointsBalance())
                .orElse(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP));

        return new LoyaltyBalanceResponse(balance, earned, redeemed);
    }

    @Transactional(Transactional.TxType.SUPPORTS)
    public List<LoyaltyTransactionDto> getTransactions(Long clientId) {
        return transactionRepository.findByClient_IdOrderByCreatedAtDesc(clientId).stream()
                .map(tx -> new LoyaltyTransactionDto(
                        tx.getId(),
                        tx.getType(),
                        tx.getPoints(),
                        tx.getDescription(),
                        tx.getCreatedAt()
                ))
                .collect(Collectors.toList());
    }

    public boolean redeemCouponWithCode(Long clientId, RedeemCouponCodeRequest request) {
        if (clientId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Client id is required");
        }
        if (request == null || request.getCouponCode() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Coupon type is required");
        }
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Client not found"));
        Coupon coupon = couponRepository.findByCode(request.getCouponCode());
        if (coupon == null || !coupon.isActive()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid or inactive coupon code");
        }
        CouponAssignment assignment = new CouponAssignment();
        assignment.setClient(client);
        assignment.setCoupon(coupon);
        assignment.setAssignedAt(LocalDateTime.now());
        assignmentRepository.save(assignment);
        return true;

    }

    public CouponDto redeemCouponWithPoints(Long clientId, RedeemCouponRequest request) {
        if (clientId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Client id is required");
        }
        if (request == null || request.getType() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Coupon type is required");
        }

        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Client not found"));

        BigDecimal cost = determineCost(request);
        BigDecimal balance = Optional.ofNullable(client.getLoyaltyPointsBalance())
                .orElse(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP));
        if (balance.compareTo(cost) < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Insufficient loyalty points");
        }

        client.setLoyaltyPointsBalance(balance.subtract(cost).setScale(2, RoundingMode.HALF_UP));

        Coupon coupon = new Coupon();
        coupon.setCode(generateCouponCode(request.getType()));
        coupon.setType(request.getType());
        coupon.setPublicCoupon(false);
        coupon.setActive(true);
        coupon.setCreatedFromPoints(true);
        coupon.setCreatedFor(client);
        coupon.setPointsCost(cost);
        if (request.getType() == CouponType.PERCENTAGE_DISCOUNT) {
            coupon.setDiscountPercent(request.getDiscountPercent().setScale(2, RoundingMode.HALF_UP));
        } else {
            coupon.setDiscountPercent(null);
        }
        Coupon savedCoupon = couponRepository.save(coupon);

        CouponAssignment assignment = new CouponAssignment();
        assignment.setClient(client);
        assignment.setCoupon(savedCoupon);
        assignment.setAssignedAt(LocalDateTime.now());
        assignmentRepository.save(assignment);

        LoyaltyPointTransaction transaction = new LoyaltyPointTransaction();
        transaction.setClient(client);
        transaction.setType(LoyaltyPointTransactionType.REDEEMED);
        transaction.setPoints(cost.negate().setScale(2, RoundingMode.HALF_UP));
        transaction.setDescription("Redeemed points for coupon " + savedCoupon.getCode());
        transactionRepository.save(transaction);

        return new CouponDto(
                savedCoupon.getCode(),
                savedCoupon.getType(),
                savedCoupon.getDiscountPercent(),
                false,
                false,
                savedCoupon.isActive(),
                savedCoupon.isCreatedFromPoints(),
                assignment.getAssignedAt()
        );
    }

    private BigDecimal determineCost(RedeemCouponRequest request) {
        if (request.getType() == CouponType.FREE_DELIVERY) {
            return FREE_DELIVERY_COUPON_COST.setScale(2, RoundingMode.HALF_UP);
        }

        BigDecimal percent = Optional.ofNullable(request.getDiscountPercent())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Discount percent is required"));

        if (percent.compareTo(MIN_PERCENT_COUPON) < 0 || percent.compareTo(MAX_PERCENT_COUPON) > 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format(
                    "Percentage coupon must be between %s%% and %s%%",
                    MIN_PERCENT_COUPON.stripTrailingZeros().toPlainString(),
                    MAX_PERCENT_COUPON.stripTrailingZeros().toPlainString()
            ));
        }

        return percent.multiply(PERCENT_COUPON_COST_PER_PERCENT)
                .setScale(2, RoundingMode.HALF_UP);
    }

    private String generateCouponCode(CouponType type) {
        String prefix = type == CouponType.FREE_DELIVERY ? "FREE" : "SAVE";
        String random = RandomStringUtils.randomAlphanumeric(6).toUpperCase();
        return prefix + "-" + random;
    }
}
