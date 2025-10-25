package com.foodify.server.modules.delivery.application;

import com.foodify.server.modules.orders.application.event.OrderLifecycleEvent;
import com.foodify.server.modules.orders.domain.Order;
import com.foodify.server.modules.orders.domain.OrderStatus;
import com.foodify.server.modules.orders.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.HashSet;
import java.util.Set;

@Component
@RequiredArgsConstructor
@Slf4j
public class DriverAvailabilityLifecycleListener {

    private final OrderRepository orderRepository;
    private final DriverAvailabilityService driverAvailabilityService;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleOrderLifecycleEvent(OrderLifecycleEvent event) {
        OrderStatus newStatus = event.getNewStatus();
        if (newStatus != OrderStatus.CANCELED && newStatus != OrderStatus.DELIVERED) {
            return;
        }

        orderRepository.findDetailedById(event.getOrderId()).ifPresentOrElse(order -> {
            Set<Long> processedDrivers = new HashSet<>();

            releaseDeliveryDriver(order, processedDrivers);
            boolean pendingDriverCleared = releasePendingDriver(order, processedDrivers, newStatus);

            if (pendingDriverCleared) {
                orderRepository.save(order);
            }
        }, () -> log.warn("Order {} not found while handling driver availability", event.getOrderId()));
    }

    private void releaseDeliveryDriver(Order order, Set<Long> processedDrivers) {
        if (order.getDelivery() == null || order.getDelivery().getDriver() == null) {
            return;
        }

        Long driverId = order.getDelivery().getDriver().getId();
        if (driverId == null || processedDrivers.contains(driverId)) {
            return;
        }

        processedDrivers.add(driverId);
        driverAvailabilityService.refreshAvailability(driverId);
        log.debug("Released delivery driver {} for order {}", driverId, order.getId());
    }

    private boolean releasePendingDriver(Order order, Set<Long> processedDrivers, OrderStatus newStatus) {
        if (order.getPendingDriver() == null) {
            return false;
        }

        Long driverId = order.getPendingDriver().getId();
        if (driverId != null && !processedDrivers.contains(driverId)) {
            processedDrivers.add(driverId);
            driverAvailabilityService.refreshAvailability(driverId);
            log.debug("Released pending driver {} after order {} transitioned to {}", driverId, order.getId(), newStatus);
        } else if (driverId == null) {
            log.debug("Clearing pending driver without identifier after order {} transitioned to {}", order.getId(), newStatus);
        }

        order.setPendingDriver(null);
        return true;
    }
}
