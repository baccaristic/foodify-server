package com.foodify.server.modules.orders.readmodel;

import com.foodify.server.modules.orders.repository.OrderRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

@Configuration
@ConditionalOnProperty(prefix = "app.orders.tracking", name = "enabled", havingValue = "true")
@EnableConfigurationProperties(OrderTrackingProperties.class)
public class OrderTrackingConfiguration {

    @Bean
    public GenericJackson2JsonRedisSerializer orderTrackingRedisSerializer(Jackson2ObjectMapperBuilder objectMapperBuilder) {
        ObjectMapper objectMapper = objectMapperBuilder
                .createXmlMapper(false)
                .build();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return new GenericJackson2JsonRedisSerializer(objectMapper);
    }

    @Bean
    public RedisTemplate<String, OrderTrackingView> orderTrackingRedisTemplate(RedisConnectionFactory connectionFactory,
                                                                              GenericJackson2JsonRedisSerializer orderTrackingRedisSerializer) {
        RedisTemplate<String, OrderTrackingView> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(orderTrackingRedisSerializer);
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(orderTrackingRedisSerializer);
        template.afterPropertiesSet();
        return template;
    }

    @Bean
    public OrderTrackingViewRepository orderTrackingViewRepository(RedisTemplate<String, OrderTrackingView> orderTrackingRedisTemplate,
                                                                   OrderTrackingProperties properties) {
        return new RedisOrderTrackingViewRepository(orderTrackingRedisTemplate, properties);
    }

    @Bean
    public OrderTrackingProjection orderTrackingProjection(OrderRepository orderRepository,
                                                           OrderTrackingViewRepository orderTrackingViewRepository) {
        return new OrderTrackingProjection(orderRepository, orderTrackingViewRepository);
    }

    @Bean
    public OrderTrackingQueryService orderTrackingQueryService(OrderTrackingViewRepository orderTrackingViewRepository) {
        return new OrderTrackingQueryService(orderTrackingViewRepository);
    }

    @Bean
    @ConditionalOnProperty(prefix = "app.orders.tracking.kafka", name = "enabled", havingValue = "true")
    public OrderTrackingMessageListener orderTrackingMessageListener(OrderTrackingProjection projection) {
        return new OrderTrackingMessageListener(projection);
    }
}
