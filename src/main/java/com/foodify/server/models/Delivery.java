package com.foodify.server.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Delivery {
    @Id @GeneratedValue
    private Long id;

    @OneToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne
    private Driver driver;

    private Long deliveryTime;
    private Long timeToPickUp;

    private LocalDateTime pickupTime;
    private LocalDateTime deliveredTime;
}