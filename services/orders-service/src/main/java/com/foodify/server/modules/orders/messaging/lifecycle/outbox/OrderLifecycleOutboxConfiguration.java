package com.foodify.server.modules.orders.messaging.lifecycle.outbox;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.foodify.server.modules.orders.messaging.lifecycle.OrderLifecycleMessagePublisher;
import com.foodify.server.modules.orders.messaging.lifecycle.OrderLifecycleMessageSender;
import com.foodify.server.modules.orders.messaging.lifecycle.OrderMessagingProperties;
import com.foodify.server.modules.orders.repository.OrderRepository;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.observation.ObservationRegistry;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class OrderLifecycleOutboxConfiguration {

    @Bean
    public OrderLifecycleOutboxService orderLifecycleOutboxService(OrderLifecycleOutboxRepository repository,
                                                                  ObjectMapper objectMapper,
                                                                  OrderMessagingProperties properties) {
        return new OrderLifecycleOutboxService(repository, objectMapper, properties);
    }

    @Bean
    public OrderLifecycleOutboxBackfillService orderLifecycleOutboxBackfillService(OrderRepository orderRepository,
                                                                                  OrderLifecycleOutboxRepository outboxRepository,
                                                                                  OrderLifecycleOutboxService outboxService,
                                                                                  OrderMessagingProperties properties) {
        return new OrderLifecycleOutboxBackfillService(orderRepository, outboxRepository, outboxService, properties);
    }

    @Bean
    @Primary
    @ConditionalOnProperty(prefix = "app.messaging.orders.outbox", name = "enabled", havingValue = "true")
    public OrderLifecycleMessagePublisher outboxOrderLifecycleMessagePublisher(OrderLifecycleOutboxService outboxService) {
        return new OutboxOrderLifecycleMessagePublisher(outboxService);
    }

    @Bean
    @ConditionalOnProperty(prefix = "app.messaging.orders.outbox.backfill", name = "enabled", havingValue = "true")
    public OrderLifecycleOutboxBackfillRunner orderLifecycleOutboxBackfillRunner(OrderLifecycleOutboxBackfillService backfillService) {
        return new OrderLifecycleOutboxBackfillRunner(backfillService);
    }

    @Bean
    @ConditionalOnProperty(prefix = "app.messaging.orders.outbox", name = "enabled", havingValue = "true")
    public OrderLifecycleOutboxProcessor orderLifecycleOutboxProcessor(OrderLifecycleOutboxService outboxService,
                                                                      OrderLifecycleMessageSender sender,
                                                                      OrderMessagingProperties properties,
                                                                      MeterRegistry meterRegistry,
                                                                      ObservationRegistry observationRegistry) {
        return new OrderLifecycleOutboxProcessor(outboxService, sender, properties, meterRegistry, observationRegistry);
    }
}
