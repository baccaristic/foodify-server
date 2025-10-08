package com.foodify.server.modules.orders.messaging.lifecycle.outbox;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "order_lifecycle_outbox")
@Getter
@Setter
public class OrderLifecycleOutbox {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "event_id", nullable = false, unique = true, updatable = false)
    private UUID eventId;

    @Column(name = "order_id", nullable = false)
    private Long orderId;

    @Column(name = "event_type", nullable = false)
    private String messageType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderLifecycleOutboxStatus status;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String payload;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "available_at", nullable = false)
    private Instant availableAt;

    @Column(name = "last_attempt_at")
    private Instant lastAttemptAt;

    @Column(name = "dispatched_at")
    private Instant dispatchedAt;

    @Column(name = "attempts", nullable = false)
    private int attempts;

    @Column(name = "last_error", columnDefinition = "TEXT")
    private String lastError;

    @PrePersist
    void prePersist() {
        Instant now = Instant.now();
        if (createdAt == null) {
            createdAt = now;
        }
        if (availableAt == null) {
            availableAt = now;
        }
        if (status == null) {
            status = OrderLifecycleOutboxStatus.PENDING;
        }
    }
}
