package com.foodify.server.modules.orders.readmodel;

import com.foodify.server.modules.orders.application.event.OrderLifecycleEvent;
import com.foodify.server.modules.orders.domain.Order;
import com.foodify.server.modules.orders.messaging.lifecycle.OrderLifecycleMessage;
import com.foodify.server.modules.orders.messaging.lifecycle.OrderLifecycleMessageFactory;
import com.foodify.server.modules.orders.repository.OrderRepository;

import java.util.Optional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@RequiredArgsConstructor
public class OrderTrackingProjection {

    private final OrderRepository orderRepository;
    private final OrderTrackingViewRepository viewRepository;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleLifecycleEvent(OrderLifecycleEvent event) {
        if (event == null) {
            return;
        }
        Optional<Order> orderOptional = orderRepository.findById(event.getOrderId());
        if (orderOptional.isEmpty()) {
            log.debug("Skipping tracking projection, order {} not found", event.getOrderId());
            return;
        }
        Order order = orderOptional.get();
        OrderLifecycleMessage message = buildMessage(order, event);
        project(message);
    }

    public void project(OrderLifecycleMessage message) {
        if (message == null) {
            log.debug("Skipping tracking projection, message was null");
            return;
        }
        OrderTrackingView current = viewRepository.find(message.orderId()).orElse(null);
        OrderTrackingView projected = OrderTrackingViewFactory.project(message, current);
        viewRepository.save(projected);
        String status = message.currentStatus() != null ? message.currentStatus() : message.previousStatus();
        log.debug("Updated order tracking view for {} with status {}", message.orderId(), status);
    }

    private OrderLifecycleMessage buildMessage(Order order, OrderLifecycleEvent event) {
        if (event.getPreviousStatus() == null) {
            return OrderLifecycleMessageFactory.created(order, event.getChangedBy());
        }
        return OrderLifecycleMessageFactory.statusChanged(
                order,
                event.getPreviousStatus(),
                event.getNewStatus(),
                event.getChangedBy(),
                event.getReason()
        );
    }
}
