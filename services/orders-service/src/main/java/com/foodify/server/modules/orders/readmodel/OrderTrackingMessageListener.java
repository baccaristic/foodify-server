package com.foodify.server.modules.orders.readmodel;

import com.foodify.server.modules.orders.messaging.lifecycle.OrderLifecycleMessage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;

@Slf4j
@RequiredArgsConstructor
public class OrderTrackingMessageListener {

    private final OrderTrackingProjection projection;

    @KafkaListener(
            topics = "${app.messaging.orders.lifecycle-topic:orders.lifecycle}",
            groupId = "${app.orders.tracking.kafka.group-id:order-tracking-projection}",
            concurrency = "${app.orders.tracking.kafka.concurrency:1}"
    )
    public void handle(OrderLifecycleMessage message) {
        if (message == null) {
            log.debug("Received null order lifecycle message, skipping projection update");
            return;
        }
        projection.project(message);
    }
}
