package com.foodify.server.modules.orders.messaging.lifecycle.outbox;

import com.foodify.server.modules.orders.messaging.lifecycle.OrderLifecycleMessage;
import com.foodify.server.modules.orders.messaging.lifecycle.OrderLifecycleMessageSender;
import com.foodify.server.modules.orders.messaging.lifecycle.OrderMessagingProperties;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class OrderLifecycleOutboxProcessor {

    private static final String METRIC_DISPATCHED = "app.orders.outbox.dispatched";
    private static final String METRIC_FAILED = "app.orders.outbox.failed";
    private static final String METRIC_SKIPPED = "app.orders.outbox.skipped";

    private final OrderLifecycleOutboxService outboxService;
    private final OrderLifecycleMessageSender sender;
    private final OrderMessagingProperties properties;
    private final MeterRegistry meterRegistry;
    private final ObservationRegistry observationRegistry;

    @Scheduled(fixedDelayString = "${app.messaging.orders.outbox.poll-interval:15000}")
    public void publishPendingMessages() {
        if (!properties.outbox().enabled() || !properties.outbox().dispatcherEnabled()) {
            incrementCounter(METRIC_SKIPPED, "reason", "disabled");
            return;
        }
        List<OrderLifecycleOutbox> pending = outboxService.fetchReadyBatch();
        if (pending.isEmpty()) {
            incrementCounter(METRIC_SKIPPED, "reason", "empty");
            return;
        }
        pending.forEach(this::dispatch);
    }

    private void dispatch(OrderLifecycleOutbox entry) {
        OrderLifecycleMessage message = outboxService.toMessage(entry);
        Observation observation = Observation.createNotStarted("orders.outbox.dispatch", observationRegistry)
                .contextualName("orders.outbox.dispatch")
                .lowCardinalityKeyValue("orders.outbox.message.type", message.type());

        observation.start();
        try (Observation.Scope scope = observation.openScope()) {
            sender.send(message);
            outboxService.markDispatched(entry.getId());
            incrementCounter(METRIC_DISPATCHED, "message.type", message.type());
            observation.event(() -> "orders.outbox.dispatched");
        } catch (Exception ex) {
            observation.error(ex);
            log.error("Failed to dispatch order lifecycle outbox event {}", entry.getEventId(), ex);
            incrementCounter(METRIC_FAILED, "message.type", message.type());
            outboxService.markFailed(entry.getId(), ex);
        } finally {
            observation.stop();
        }
    }

    private void incrementCounter(String name, String... tags) {
        meterRegistry.counter(name, tags).increment();
    }
}
