package com.foodify.server.modules.orders.readmodel;

import com.foodify.server.modules.orders.repository.OrderRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@ConditionalOnProperty(prefix = "app.orders.tracking", name = "enabled", havingValue = "true")
@EnableConfigurationProperties(OrderTrackingProperties.class)
public class OrderTrackingConfiguration {

    @Bean
    public RedisTemplate<String, OrderTrackingView> orderTrackingRedisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, OrderTrackingView> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
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
}
