package com.foodify.server.modules.restaurants.domain;

import com.foodify.server.modules.identity.domain.Client;
import com.foodify.server.modules.orders.domain.Order;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "restaurant_ratings",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_restaurant_rating_order", columnNames = {"order_id"})
        },
        indexes = {
                @Index(name = "idx_restaurant_rating_restaurant", columnList = "restaurant_id"),
                @Index(name = "idx_restaurant_rating_client", columnList = "client_id")
        }
)
@Getter
@Setter
public class RestaurantRating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "order_id", nullable = false, unique = true)
    private Order order;

    @Column(name = "thumbs_up", nullable = false)
    private Boolean thumbsUp;

    @Column(name = "comments", length = 1024)
    private String comments;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        if (this.createdAt == null) {
            this.createdAt = now;
        }
        this.updatedAt = now;
    }

    @PreUpdate
    void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
