package com.foodify.server.modules.delivery.application.signals;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DriverCapacityService {

    private static final String CAPACITY_KEY_PREFIX = "driver:capacity:";
    private static final double DEFAULT_CAPACITY = 1.0;

    private final StringRedisTemplate redisTemplate;

    public double resolveCapacityFactor(Long driverId) {
        if (driverId == null) {
            return DEFAULT_CAPACITY;
        }
        String rawValue = redisTemplate.opsForValue().get(CAPACITY_KEY_PREFIX + driverId);
        if (rawValue == null) {
            return DEFAULT_CAPACITY;
        }
        try {
            double parsed = Double.parseDouble(rawValue);
            if (Double.isNaN(parsed) || Double.isInfinite(parsed)) {
                return DEFAULT_CAPACITY;
            }
            return Math.max(0.0, Math.min(1.0, parsed));
        } catch (NumberFormatException ex) {
            return DEFAULT_CAPACITY;
        }
    }
}
