package com.foodify.server.modules.delivery.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "delivery_ratings")
@Getter
@Setter
public class DeliveryRating {

    @Id
    @Column(name = "delivery_id")
    private Long deliveryId;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    @Column(name = "timing_rating", nullable = false)
    private Integer timingRating;

    @Column(name = "food_condition_rating", nullable = false)
    private Integer foodConditionRating;

    @Column(name = "professionalism_rating", nullable = false)
    private Integer professionalismRating;

    @Column(name = "overall_rating", nullable = false)
    private Integer overallRating;

    @Column(name = "comments", length = 1024)
    private String comments;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
