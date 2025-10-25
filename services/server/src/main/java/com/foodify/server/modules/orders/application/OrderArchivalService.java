package com.foodify.server.modules.orders.application;

import com.foodify.server.modules.orders.domain.Order;
import com.foodify.server.modules.orders.domain.OrderStatus;
import com.foodify.server.modules.orders.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderArchivalService {

    private static final List<OrderStatus> ARCHIVABLE_STATUSES = List.of(
            OrderStatus.DELIVERED,
            OrderStatus.CANCELED,
            OrderStatus.REJECTED
    );

    private final OrderRepository orderRepository;
    private final OrderLifecycleService orderLifecycleService;

    @Value("${orders.archive.completed-after-hours:72}")
    private long archiveAfterHours;

    @Value("${orders.archive.batch-size:200}")
    private int archiveBatchSize;

    @Scheduled(fixedDelayString = "${orders.archive.fixed-delay-ms:3600000}")
    @Transactional
    public void archiveCompletedOrders() {
        LocalDateTime threshold = LocalDateTime.now().minusHours(archiveAfterHours);
        int batchSize = Math.max(archiveBatchSize, 1);
        Pageable pageable = PageRequest.of(0, batchSize);

        int archivedCount = 0;
        while (true) {
            Slice<Order> slice = orderRepository.findArchivableOrders(ARCHIVABLE_STATUSES, threshold, pageable);
            if (slice.isEmpty()) {
                break;
            }

            LocalDateTime archivedAt = LocalDateTime.now();
            List<Order> batch = slice.getContent();
            batch.forEach(order -> order.setArchivedAt(archivedAt));
            orderRepository.saveAll(batch);
            batch.forEach(order -> orderLifecycleService.recordArchive(order, "system:archiver", "Order archived automatically"));
            archivedCount += batch.size();
        }

        if (archivedCount > 0) {
            log.info("Archived {} completed orders", archivedCount);
        }
    }
}
