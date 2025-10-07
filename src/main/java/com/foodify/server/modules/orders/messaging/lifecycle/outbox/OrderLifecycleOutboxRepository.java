package com.foodify.server.modules.orders.messaging.lifecycle.outbox;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;

public interface OrderLifecycleOutboxRepository extends JpaRepository<OrderLifecycleOutbox, Long> {

    @Query("SELECT o FROM OrderLifecycleOutbox o WHERE o.status = :status AND (o.availableAt IS NULL OR o.availableAt <= :now) ORDER BY o.createdAt ASC")
    List<OrderLifecycleOutbox> findReady(@Param("status") OrderLifecycleOutboxStatus status,
                                         @Param("now") Instant now,
                                         Pageable pageable);
}
