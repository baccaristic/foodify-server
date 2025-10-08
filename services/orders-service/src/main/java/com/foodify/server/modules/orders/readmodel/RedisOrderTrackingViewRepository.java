package com.foodify.server.modules.orders.readmodel;

import java.time.Duration;
import java.util.Optional;

import org.springframework.data.redis.core.RedisTemplate;

public class RedisOrderTrackingViewRepository implements OrderTrackingViewRepository {

    private final RedisTemplate<String, OrderTrackingView> redisTemplate;
    private final OrderTrackingProperties properties;

    public RedisOrderTrackingViewRepository(RedisTemplate<String, OrderTrackingView> redisTemplate,
                                            OrderTrackingProperties properties) {
        this.redisTemplate = redisTemplate;
        this.properties = properties;
    }

    @Override
    public Optional<OrderTrackingView> find(Long orderId) {
        if (orderId == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(redisTemplate.opsForValue().get(key(orderId)));
    }

    @Override
    public void save(OrderTrackingView view) {
        if (view == null || view.orderId() == null) {
            return;
        }
        Duration ttl = properties.getTtl();
        if (ttl != null && !ttl.isZero() && !ttl.isNegative()) {
            redisTemplate.opsForValue().set(key(view.orderId()), view, ttl);
        } else {
            redisTemplate.opsForValue().set(key(view.orderId()), view);
        }
    }

    @Override
    public void delete(Long orderId) {
        if (orderId == null) {
            return;
        }
        redisTemplate.delete(key(orderId));
    }

    private String key(Long orderId) {
        return properties.getKeyPrefix() + orderId;
    }
}
