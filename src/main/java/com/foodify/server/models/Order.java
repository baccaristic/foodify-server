package com.foodify.server.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.foodify.server.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Auditable;

import java.time.LocalDateTime;
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

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> items;

    private LocalDateTime orderTime;

    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL)
    private Delivery delivery;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    private LocalDateTime date;

    @ManyToOne
    @JoinColumn(name = "pending_driver_id")
    @JsonIgnore
    private Driver pendingDriver;
}