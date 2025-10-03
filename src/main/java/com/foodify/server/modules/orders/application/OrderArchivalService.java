package com.foodify.server.modules.orders.application;

import com.foodify.server.modules.orders.domain.Order;
import com.foodify.server.modules.orders.domain.OrderStatus;
import com.foodify.server.modules.orders.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderArchivalService {

    private final OrderRepository orderRepository;
    private final OrderLifecycleService orderLifecycleService;

    @Value("${orders.archive.completed-after-hours:72}")
    private long archiveAfterHours;

    @Scheduled(fixedDelayString = "${orders.archive.fixed-delay-ms:3600000}")
    @Transactional
    public void archiveCompletedOrders() {
        LocalDateTime threshold = LocalDateTime.now().minusHours(archiveAfterHours);
        List<Order> candidates = orderRepository.findAllByStatusInAndArchivedAtIsNullAndDateBefore(
                List.of(OrderStatus.DELIVERED, OrderStatus.CANCELED, OrderStatus.REJECTED),
                threshold
        );

        if (candidates.isEmpty()) {
            return;
        }

        LocalDateTime archivedAt = LocalDateTime.now();
        candidates.forEach(order -> order.setArchivedAt(archivedAt));
        orderRepository.saveAll(candidates);
        candidates.forEach(order -> orderLifecycleService.recordArchive(order, "system:archiver", "Order archived automatically"));
        log.info("Archived {} completed orders", candidates.size());
    }
}
