package com.foodify.server.modules.rewards.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.foodify.server.modules.identity.domain.Client;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "coupons", uniqueConstraints = {
        @UniqueConstraint(name = "uk_coupon_code", columnNames = "code")
})
@Getter
@Setter
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 64)
    private String code;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 32)
    private CouponType type;

    @Column(name = "discount_percent", precision = 5, scale = 2)
    private BigDecimal discountPercent;

    @Column(name = "is_public", nullable = false)
    private boolean publicCoupon = false;

    @Column(nullable = false)
    private boolean active = true;

    @Column(name = "points_cost", precision = 19, scale = 2)
    private BigDecimal pointsCost;

    @Column(name = "created_from_points", nullable = false)
    private boolean createdFromPoints = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "created_for_client_id")
    private Client createdFor;

    @OneToMany(mappedBy = "coupon")
    @JsonIgnore
    private List<CouponAssignment> assignments = new ArrayList<>();

    @OneToMany(mappedBy = "coupon")
    @JsonIgnore
    private List<CouponRedemption> redemptions = new ArrayList<>();
}
