package com.foodify.server.modules.orders.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.foodify.server.modules.addresses.domain.SavedAddress;
import com.foodify.server.modules.delivery.domain.Delivery;
import com.foodify.server.modules.identity.domain.Client;
import com.foodify.server.modules.identity.domain.Driver;
import com.foodify.server.modules.restaurants.domain.Restaurant;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@Setter
public class Order  {
    @Id @GeneratedValue
    private Long id;

    @ManyToOne
    private Client client;

    @JsonIgnoreProperties({"orders", "admin", "menu"})
    @ManyToOne
    private Restaurant restaurant;

    private String deliveryAddress;

    private String paymentMethod;

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

    @Column(name = "order_total", precision = 12, scale = 2)
    private BigDecimal total;
}
