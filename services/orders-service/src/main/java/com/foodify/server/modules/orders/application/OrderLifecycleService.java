package com.foodify.server.modules.orders.application;

import com.foodify.server.modules.orders.application.event.OrderLifecycleEvent;
import com.foodify.server.modules.orders.domain.Order;
import com.foodify.server.modules.orders.domain.OrderLifecycleAction;
import com.foodify.server.modules.orders.domain.OrderStatus;
import com.foodify.server.modules.orders.domain.OrderStatusHistory;
import com.foodify.server.modules.orders.messaging.lifecycle.OrderLifecycleMessagePublisher;
import com.foodify.server.modules.orders.repository.OrderRepository;
import com.foodify.server.modules.orders.repository.OrderStatusHistoryRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderLifecycleService {

    private static final Map<OrderStatus, Set<OrderStatus>> ALLOWED_TRANSITIONS = buildAllowedTransitions();

    private final OrderRepository orderRepository;
    private final OrderStatusHistoryRepository historyRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final OrderLifecycleMessagePublisher messagePublisher;

    @Transactional
    public void registerCreation(Order order, String changedBy) {
        Objects.requireNonNull(order, "Order must not be null");
        logLifecycleAction(order, OrderLifecycleAction.CREATED, null, order.getStatus(), changedBy, "Order created", null);
        publishCreationEvents(order, changedBy);
    }

    @Transactional
    public Order transition(Long orderId, OrderStatus newStatus, String changedBy, String reason) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));
        return transition(order, newStatus, changedBy, reason);
    }

    @Transactional
    public Order transition(Order order, OrderStatus newStatus, String changedBy, String reason) {
        Objects.requireNonNull(order, "Order must not be null");
        Objects.requireNonNull(newStatus, "Target status must not be null");

        OrderStatus currentStatus = order.getStatus();
        if (Objects.equals(currentStatus, newStatus)) {
            log.debug("Order {} already in status {}, skipping transition", order.getId(), newStatus);
            return order;
        }

        if (!isTransitionAllowed(currentStatus, newStatus)) {
            throw new IllegalStateException(String.format(
                    "Transition from %s to %s is not allowed",
                    currentStatus,
                    newStatus
            ));
        }

        order.setStatus(newStatus);
        order.setDate(LocalDateTime.now());
        Order saved = orderRepository.save(order);
        logLifecycleAction(saved, OrderLifecycleAction.STATUS_CHANGE, currentStatus, newStatus, changedBy, reason, null);
        publishStatusEvents(saved, currentStatus, newStatus, changedBy, reason);
        log.info("Order {} transitioned from {} to {} by {}", saved.getId(), currentStatus, newStatus, changedBy);
        return saved;
    }

    @Transactional
    public void recordArchive(Order order, String changedBy, String reason) {
        Objects.requireNonNull(order, "Order must not be null");
        logLifecycleAction(order, OrderLifecycleAction.ARCHIVED, order.getStatus(), order.getStatus(), changedBy, reason, null);
        log.info("Order {} archived by {}", order.getId(), changedBy);
    }

    private void publishCreationEvents(Order order, String changedBy) {
        eventPublisher.publishEvent(new OrderLifecycleEvent(order.getId(), null, order.getStatus(), changedBy, "Order created"));
        messagePublisher.publishOrderCreated(order, changedBy);
    }

    private void publishStatusEvents(Order order,
                                     OrderStatus previous,
                                     OrderStatus current,
                                     String changedBy,
                                     String reason) {
        eventPublisher.publishEvent(new OrderLifecycleEvent(order.getId(), previous, current, changedBy, reason));
        messagePublisher.publishStatusChanged(order, previous, current, changedBy, reason);
    }

    private void logLifecycleAction(Order order,
                                    OrderLifecycleAction action,
                                    OrderStatus previousStatus,
                                    OrderStatus newStatus,
                                    String changedBy,
                                    String reason,
                                    String metadata) {
        OrderStatusHistory history = new OrderStatusHistory();
        history.setOrder(order);
        history.setAction(action);
        history.setPreviousStatus(previousStatus);
        history.setNewStatus(newStatus);
        history.setChangedBy(changedBy);
        history.setReason(reason);
        history.setMetadata(metadata);
        historyRepository.save(history);
        if (order.getStatusHistory() != null) {
            order.getStatusHistory().add(history);
        }
    }

    private static Map<OrderStatus, Set<OrderStatus>> buildAllowedTransitions() {
        Map<OrderStatus, Set<OrderStatus>> transitions = new EnumMap<>(OrderStatus.class);
        transitions.put(OrderStatus.PENDING, EnumSet.of(OrderStatus.ACCEPTED, OrderStatus.REJECTED, OrderStatus.CANCELED));
        transitions.put(OrderStatus.ACCEPTED, EnumSet.of(OrderStatus.PREPARING, OrderStatus.CANCELED));
        transitions.put(OrderStatus.PREPARING, EnumSet.of(OrderStatus.READY_FOR_PICK_UP, OrderStatus.IN_DELIVERY, OrderStatus.CANCELED));
        transitions.put(OrderStatus.READY_FOR_PICK_UP, EnumSet.of(OrderStatus.IN_DELIVERY, OrderStatus.CANCELED));
        transitions.put(OrderStatus.IN_DELIVERY, EnumSet.of(OrderStatus.DELIVERED, OrderStatus.CANCELED));
        transitions.put(OrderStatus.DELIVERED, Collections.emptySet());
        transitions.put(OrderStatus.REJECTED, Collections.emptySet());
        transitions.put(OrderStatus.CANCELED, Collections.emptySet());
        return transitions;
    }

    private boolean isTransitionAllowed(OrderStatus from, OrderStatus to) {
        if (from == null) {
            return true;
        }
        Set<OrderStatus> allowed = ALLOWED_TRANSITIONS.getOrDefault(from, Collections.emptySet());
        return allowed.contains(to);
    }
}
