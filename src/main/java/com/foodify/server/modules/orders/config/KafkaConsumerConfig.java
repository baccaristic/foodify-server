package com.foodify.server.modules.orders.config;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.foodify.server.modules.orders.dto.OrderRequest;
import com.foodify.server.modules.orders.messaging.lifecycle.OrderLifecycleMessage;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConsumerConfig {
    @Bean
    public ConsumerFactory<String, OrderLifecycleMessage> orderLifecycleConsumerFactory(KafkaProperties kafkaProperties) {
        Map<String, Object> config = new HashMap<>(kafkaProperties.buildConsumerProperties(null));
        config.put("spring.json.value.default.type", OrderLifecycleMessage.class.getName());
        config.put("spring.json.trusted.packages", "com.foodify.server.modules.orders.messaging.lifecycle,com.foodify.server.modules.orders.messaging.lifecycle.outbox");
        JsonDeserializer<OrderLifecycleMessage> deserializer = new JsonDeserializer<>(OrderLifecycleMessage.class);
        deserializer.addTrustedPackages("com.foodify.server.modules.orders.messaging.lifecycle", "com.foodify.server.modules.orders.messaging.lifecycle.outbox");
        deserializer.getObjectMapper().registerModule(new JavaTimeModule());
        return new DefaultKafkaConsumerFactory<>(config, new StringDeserializer(), deserializer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, OrderLifecycleMessage> kafkaListenerContainerFactory(
            ConsumerFactory<String, OrderLifecycleMessage> orderLifecycleConsumerFactory) {
        ConcurrentKafkaListenerContainerFactory<String, OrderLifecycleMessage> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(orderLifecycleConsumerFactory);
        return factory;
    }

    @Bean
    public ConsumerFactory<String, OrderRequest> orderRequestConsumerFactory(KafkaProperties kafkaProperties) {
        Map<String, Object> config = new HashMap<>(kafkaProperties.buildConsumerProperties(null));
        config.put("spring.json.value.default.type", OrderRequest.class.getName());
        config.put("spring.json.trusted.packages", "com.foodify.server.modules.orders.dto");
        JsonDeserializer<OrderRequest> deserializer = new JsonDeserializer<>(OrderRequest.class);
        deserializer.addTrustedPackages("com.foodify.server.modules.orders.dto");
        return new DefaultKafkaConsumerFactory<>(config, new StringDeserializer(), deserializer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, OrderRequest> orderRequestKafkaListenerContainerFactory(
            ConsumerFactory<String, OrderRequest> orderRequestConsumerFactory) {
        ConcurrentKafkaListenerContainerFactory<String, OrderRequest> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(orderRequestConsumerFactory);
        return factory;
    }
}

