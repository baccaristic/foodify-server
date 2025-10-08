package com.foodify.server.modules.delivery.domain;

import com.foodify.server.modules.identity.domain.Driver;
import com.foodify.server.modules.orders.domain.Order;
import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
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
    @JoinColumn(name = "order_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Order order;

    @ManyToOne
    private Driver driver;

    private Long deliveryTime;
    private Long timeToPickUp;

    private LocalDateTime assignedTime;
    private LocalDateTime pickupTime;
    private LocalDateTime deliveredTime;
}
