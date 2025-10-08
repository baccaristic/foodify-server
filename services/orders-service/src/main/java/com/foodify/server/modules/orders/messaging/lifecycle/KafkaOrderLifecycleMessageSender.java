package com.foodify.server.modules.orders.messaging.lifecycle;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
public class KafkaOrderLifecycleMessageSender implements OrderLifecycleMessageSender {

    private final KafkaTemplate<String, OrderLifecycleMessage> kafkaTemplate;
    private final OrderMessagingProperties properties;

    @Override
    public void send(OrderLifecycleMessage message) {
        if (message.orderId() == null) {
            log.debug("Skipping order lifecycle message without order id: {}", message);
            return;
        }
        String key = Objects.toString(message.orderId());
        kafkaTemplate.send(properties.lifecycleTopic(), key, message)
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        log.error("Failed to publish order lifecycle message {}", message, ex);
                    } else if (log.isDebugEnabled()) {
                        log.debug("Published order lifecycle message {} to topic {}", message.eventId(), properties.lifecycleTopic());
                    }
                });
    }
}
