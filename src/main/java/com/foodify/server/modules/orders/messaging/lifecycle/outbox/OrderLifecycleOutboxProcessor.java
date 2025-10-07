package com.foodify.server.modules.orders.messaging.lifecycle.outbox;

import com.foodify.server.modules.orders.messaging.lifecycle.OrderLifecycleMessage;
import com.foodify.server.modules.orders.messaging.lifecycle.OrderLifecycleMessageSender;
import com.foodify.server.modules.orders.messaging.lifecycle.OrderMessagingProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class OrderLifecycleOutboxProcessor {

    private final OrderLifecycleOutboxService outboxService;
    private final OrderLifecycleMessageSender sender;
    private final OrderMessagingProperties properties;

    @Scheduled(fixedDelayString = "${app.messaging.orders.outbox.poll-interval:15000}")
    public void publishPendingMessages() {
        if (!properties.outbox().enabled()) {
            return;
        }
        List<OrderLifecycleOutbox> pending = outboxService.fetchReadyBatch();
        if (pending.isEmpty()) {
            return;
        }
        pending.forEach(this::dispatch);
    }

    private void dispatch(OrderLifecycleOutbox entry) {
        try {
            OrderLifecycleMessage message = outboxService.toMessage(entry);
            sender.send(message);
            outboxService.markDispatched(entry.getId());
        } catch (Exception ex) {
            log.error("Failed to dispatch order lifecycle outbox event {}", entry.getEventId(), ex);
            outboxService.markFailed(entry.getId(), ex);
        }
    }
}
