package com.foodify.server.modules.orders.messaging.lifecycle.outbox;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.foodify.server.modules.orders.messaging.lifecycle.OrderLifecycleMessage;
import com.foodify.server.modules.orders.messaging.lifecycle.OrderMessagingProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class OrderLifecycleOutboxService {

    private static final DateTimeFormatter ERROR_TIMESTAMP = DateTimeFormatter.ISO_OFFSET_DATE_TIME;

    private final OrderLifecycleOutboxRepository repository;
    private final ObjectMapper objectMapper;
    private final OrderMessagingProperties properties;

    @Transactional
    public void enqueue(OrderLifecycleMessage message) {
        try {
            OrderLifecycleOutbox entry = new OrderLifecycleOutbox();
            entry.setEventId(message.eventId());
            entry.setOrderId(message.orderId());
            entry.setEventType(message.eventType());
            entry.setPayload(objectMapper.writeValueAsString(message));
            entry.setStatus(OrderLifecycleOutboxStatus.PENDING);
            repository.save(entry);
            log.debug("Enqueued order lifecycle message {} for order {}", message.eventId(), message.orderId());
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Failed to serialise order lifecycle message", e);
        }
    }

    @Transactional(readOnly = true)
    public List<OrderLifecycleOutbox> fetchReadyBatch() {
        int batchSize = properties.outbox().batchSize();
        return repository.findReady(OrderLifecycleOutboxStatus.PENDING, Instant.now(), PageRequest.of(0, batchSize));
    }

    public OrderLifecycleMessage toMessage(OrderLifecycleOutbox entry) {
        try {
            return objectMapper.readValue(entry.getPayload(), OrderLifecycleMessage.class);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Failed to deserialise order lifecycle message payload", e);
        }
    }

    @Transactional
    public void markDispatched(Long id) {
        repository.findById(id).ifPresent(entry -> {
            Instant now = Instant.now();
            entry.setStatus(OrderLifecycleOutboxStatus.DISPATCHED);
            entry.setDispatchedAt(now);
            entry.setLastAttemptAt(now);
            entry.setAttempts(entry.getAttempts() + 1);
            entry.setLastError(null);
        });
    }

    @Transactional
    public void markFailed(Long id, Exception exception) {
        repository.findById(id).ifPresent(entry -> {
            Instant now = Instant.now();
            entry.setStatus(OrderLifecycleOutboxStatus.PENDING);
            entry.setAvailableAt(now.plusMillis(properties.outbox().retryDelay()));
            entry.setLastAttemptAt(now);
            entry.setAttempts(entry.getAttempts() + 1);
            entry.setLastError(formatError(exception));
        });
    }

    private String formatError(Exception exception) {
        String message = exception.getMessage();
        if (message == null || message.isBlank()) {
            message = exception.getClass().getSimpleName();
        }
        return "%s – %s".formatted(ERROR_TIMESTAMP.format(Instant.now().atOffset(ZoneOffset.UTC)), truncate(message));
    }

    private String truncate(String value) {
        if (value == null) {
            return null;
        }
        return value.length() > 500 ? value.substring(0, 500) : value;
    }
}
