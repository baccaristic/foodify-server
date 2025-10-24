package com.foodify.server.modules.rewards.application;

import com.foodify.server.modules.orders.application.event.OrderLifecycleEvent;
import com.foodify.server.modules.orders.domain.Order;
import com.foodify.server.modules.orders.domain.OrderStatus;
import com.foodify.server.modules.orders.repository.OrderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
@Slf4j
public class LoyaltyOrderEventListener {

    private final OrderRepository orderRepository;
    private final LoyaltyService loyaltyService;

    @Async("orderLifecycleExecutor")
    @Transactional
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(OrderLifecycleEvent event) {
        if (event.getNewStatus() != OrderStatus.DELIVERED) {
            return;
        }

        orderRepository.findById(event.getOrderId()).ifPresent(order -> {
            try {
                loyaltyService.awardPointsForDeliveredOrder(order);
                orderRepository.save(order);
            } catch (Exception ex) {
                log.warn("Failed to award loyalty points for order {}", order.getId(), ex);
            }
        });
    }
}
