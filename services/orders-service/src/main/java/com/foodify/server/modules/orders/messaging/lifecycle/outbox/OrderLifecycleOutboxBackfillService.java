package com.foodify.server.modules.orders.messaging.lifecycle.outbox;

import com.foodify.server.modules.orders.domain.Order;
import com.foodify.server.modules.orders.messaging.lifecycle.OrderLifecycleMessage;
import com.foodify.server.modules.orders.messaging.lifecycle.OrderLifecycleMessageFactory;
import com.foodify.server.modules.orders.messaging.lifecycle.OrderMessagingProperties;
import com.foodify.server.modules.orders.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class OrderLifecycleOutboxBackfillService {

    private final OrderRepository orderRepository;
    private final OrderLifecycleOutboxRepository outboxRepository;
    private final OrderLifecycleOutboxService outboxService;
    private final OrderMessagingProperties properties;

    @Transactional
    public OrderLifecycleOutboxBackfillSummary backfillCreatedEvents() {
        OrderMessagingProperties.BackfillProperties backfillProperties = properties.backfill();
        int chunkSize = backfillProperties.chunkSize();
        boolean dryRun = backfillProperties.dryRun();
        String actor = backfillProperties.actor();

        long processed = 0;
        long enqueued = 0;
        long skipped = 0;

        Pageable pageable = PageRequest.of(0, chunkSize, Sort.by(Sort.Direction.ASC, "id"));
        Page<Order> page;
        do {
            page = orderRepository.findAll(pageable);
            List<Order> orders = page.getContent();
            for (Order order : orders) {
                processed++;
                if (outboxRepository.existsByOrderIdAndMessageType(order.getId(), OrderLifecycleMessageFactory.EVENT_TYPE_CREATED)) {
                    skipped++;
                    continue;
                }

                OrderLifecycleMessage message = OrderLifecycleMessageFactory.created(order, actor);
                if (!dryRun) {
                    outboxService.enqueue(message);
                }
                enqueued++;
            }
            pageable = pageable.next();
        } while (page.hasNext());

        log.info("Order lifecycle outbox backfill complete: processed={}, enqueued={}, skipped={}, dryRun={}",
                processed, enqueued, skipped, dryRun);

        return new OrderLifecycleOutboxBackfillSummary(processed, enqueued, skipped, dryRun);
    }
}
