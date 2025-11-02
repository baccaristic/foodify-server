package com.foodify.server.modules.orders.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.foodify.server.modules.addresses.domain.SavedAddress;
import com.foodify.server.modules.delivery.domain.Delivery;
import com.foodify.server.modules.identity.domain.Client;
import com.foodify.server.modules.identity.domain.Driver;
import com.foodify.server.modules.restaurants.domain.Restaurant;
import com.foodify.server.modules.rewards.domain.Coupon;
import jakarta.persistence.*;
import jakarta.persistence.NamedAttributeNode;
import jakarta.persistence.NamedEntityGraph;
import jakarta.persistence.NamedSubgraph;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NamedEntityGraph(
        name = "Order.summary",
        attributeNodes = {
                @NamedAttributeNode("client"),
                @NamedAttributeNode(value = "restaurant", subgraph = "order.restaurant"),
                @NamedAttributeNode(value = "delivery", subgraph = "order.delivery"),
                @NamedAttributeNode("pendingDriver"),
                @NamedAttributeNode(value = "items", subgraph = "order.items"),
                @NamedAttributeNode("savedAddress")
        },
        subgraphs = {
                @NamedSubgraph(name = "order.restaurant", attributeNodes = @NamedAttributeNode("admin")),
                @NamedSubgraph(name = "order.delivery", attributeNodes = @NamedAttributeNode("driver")),
                @NamedSubgraph(name = "order.items", attributeNodes = {
                        @NamedAttributeNode("menuItem"),
                        @NamedAttributeNode("menuItemExtras")
                })
        }
)
@Entity
@Table(name = "orders")
@Getter
@Setter
public class Order  {
    public static final String SUMMARY_GRAPH = "Order.summary";
    @Id @GeneratedValue
    private Long id;

    @ManyToOne
    private Client client;

    @JsonIgnoreProperties({"orders", "admin", "menu"})
    @ManyToOne
    private Restaurant restaurant;

    private String deliveryAddress;

    private String paymentMethod;

    @Column(name = "payment_reference")
    private String paymentReference;

    @Column(name = "payment_url")
    private String paymentUrl;

    @Column(name = "payment_status")
    private String paymentStatus;

    @Column(name = "payment_environment")
    private String paymentEnvironment;

    @Column(name = "payment_expires_at")
    private LocalDateTime paymentExpiresAt;

    private double lng;
    private double lat;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "saved_address_id")
    private SavedAddress savedAddress;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items = new ArrayList<>();

    private LocalDateTime orderTime;

    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private Delivery delivery;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    private LocalDateTime date;

    @Column(name = "archived_at")
    private LocalDateTime archivedAt;

    @ManyToOne
    @JoinColumn(name = "pending_driver_id")
    @JsonIgnore
    private Driver pendingDriver;

    private String pickupToken;

    private String deliveryToken;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<OrderStatusHistory> statusHistory = new ArrayList<>();

    @Column(name = "items_subtotal", precision = 12, scale = 2)
    private BigDecimal itemsSubtotal;

    @Column(name = "extras_total", precision = 12, scale = 2)
    private BigDecimal extrasTotal;

    @Column(name = "promotion_discount", precision = 12, scale = 2)
    private BigDecimal promotionDiscount;

    @Column(name = "items_total", precision = 12, scale = 2)
    private BigDecimal itemsTotal;

    @Column(name = "delivery_fee", precision = 12, scale = 2)
    private BigDecimal deliveryFee;

    @Column(name = "service_fee", precision = 12, scale = 2)
    private BigDecimal serviceFee;

    @Column(name = "order_total", precision = 12, scale = 2)
    private BigDecimal total;

    @Column(name = "tip_percentage", precision = 5, scale = 2)
    private BigDecimal tipPercentage;

    @Column(name = "tip_amount", precision = 12, scale = 2)
    private BigDecimal tipAmount;

    @Column(name = "cash_to_collect", precision = 12, scale = 2)
    private BigDecimal cashToCollect;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_id")
    @JsonIgnoreProperties({"assignments", "redemptions"})
    private Coupon coupon;

    @Column(name = "coupon_discount", precision = 12, scale = 2)
    private BigDecimal couponDiscount;

    @Column(name = "loyalty_points_earned", precision = 19, scale = 2)
    private BigDecimal loyaltyPointsEarned;

    @Column(name = "estimated_ready_at")
    private LocalDateTime estimatedReadyAt;
}

