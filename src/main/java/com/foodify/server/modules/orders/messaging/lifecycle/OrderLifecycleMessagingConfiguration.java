package com.foodify.server.modules.orders.messaging.lifecycle;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;

@Configuration
@EnableConfigurationProperties(OrderMessagingProperties.class)
public class OrderLifecycleMessagingConfiguration {

    @Bean
    @ConditionalOnBean(name = "orderLifecycleKafkaTemplate")
    public OrderLifecycleMessagePublisher kafkaOrderLifecycleMessagePublisher(
            @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
            @Qualifier("orderLifecycleKafkaTemplate")
            KafkaTemplate<String, OrderLifecycleMessage> orderLifecycleKafkaTemplate,
            OrderMessagingProperties properties) {
        return new KafkaOrderLifecycleMessagePublisher(orderLifecycleKafkaTemplate, properties);
    }

    @Bean
    @ConditionalOnMissingBean(OrderLifecycleMessagePublisher.class)
    public OrderLifecycleMessagePublisher noOpOrderLifecycleMessagePublisher() {
        return new NoOpOrderLifecycleMessagePublisher();
    }
}
