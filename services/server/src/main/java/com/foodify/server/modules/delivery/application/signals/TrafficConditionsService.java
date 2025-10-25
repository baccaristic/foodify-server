package com.foodify.server.modules.delivery.application.signals;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TrafficConditionsService {

    private static final String DRIVER_TRAFFIC_KEY_PREFIX = "traffic:driver:";
    private static final String GLOBAL_TRAFFIC_KEY = "traffic:global";
    private static final double DEFAULT_MULTIPLIER = 1.0;

    private final StringRedisTemplate redisTemplate;

    public double resolveMultiplier(Long driverId) {
        Double driverSpecific = readMultiplier(DRIVER_TRAFFIC_KEY_PREFIX + driverId);
        if (driverSpecific != null) {
            return clamp(driverSpecific);
        }
        Double global = readMultiplier(GLOBAL_TRAFFIC_KEY);
        return clamp(global != null ? global : DEFAULT_MULTIPLIER);
    }

    private Double readMultiplier(String key) {
        String value = redisTemplate.opsForValue().get(key);
        if (value == null) {
            return null;
        }
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    private double clamp(double value) {
        if (Double.isNaN(value) || Double.isInfinite(value)) {
            return DEFAULT_MULTIPLIER;
        }
        return Math.max(0.5, Math.min(3.0, value));
    }
}
