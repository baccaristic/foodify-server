package com.foodify.server.modules.orders.config;

import com.foodify.server.modules.orders.dto.OrderRequest;
import com.foodify.server.modules.orders.messaging.lifecycle.OrderLifecycleMessage;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaProducerConfig {

    @Bean
    public ProducerFactory<String, OrderRequest> producerFactory(KafkaProperties kafkaProperties) {
        return new DefaultKafkaProducerFactory<>(producerConfigs(kafkaProperties));
    }

    @Bean
    public KafkaTemplate<String, OrderRequest> kafkaTemplate(ProducerFactory<String, OrderRequest> producerFactory) {
        return new KafkaTemplate<>(producerFactory);
    }

    @Bean
    public ProducerFactory<String, OrderLifecycleMessage> orderLifecycleProducerFactory(KafkaProperties kafkaProperties) {
        return new DefaultKafkaProducerFactory<>(producerConfigs(kafkaProperties));
    }

    @Bean(name = "orderLifecycleKafkaTemplate")
    public KafkaTemplate<String, OrderLifecycleMessage> orderLifecycleKafkaTemplate(
            ProducerFactory<String, OrderLifecycleMessage> orderLifecycleProducerFactory) {
        return new KafkaTemplate<>(orderLifecycleProducerFactory);
    }

    private Map<String, Object> producerConfigs(KafkaProperties kafkaProperties) {
        Map<String, Object> config = new HashMap<>(kafkaProperties.buildProducerProperties(null));
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        config.put(JsonSerializer.ADD_TYPE_INFO_HEADERS, false);
        return config;
    }
}