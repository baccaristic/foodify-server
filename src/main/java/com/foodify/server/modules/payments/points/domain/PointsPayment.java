package com.foodify.server.modules.payments.points.domain;

import com.foodify.server.modules.identity.domain.Client;
import com.foodify.server.modules.restaurants.domain.Restaurant;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "points_payments")
@Getter
@Setter
public class PointsPayment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id")
    private Client client;

    @Column(name = "amount_tnd", precision = 19, scale = 2, nullable = false)
    private BigDecimal amountTnd;

    @Column(name = "points_amount", precision = 19, scale = 2, nullable = false)
    private BigDecimal pointsAmount;

    @Column(name = "payment_token", unique = true, nullable = false, length = 255)
    private String paymentToken;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 32)
    private PointsPaymentStatus status = PointsPaymentStatus.PENDING;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;
}
