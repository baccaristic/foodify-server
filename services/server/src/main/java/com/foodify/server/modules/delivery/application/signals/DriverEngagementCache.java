package com.foodify.server.modules.delivery.application.signals;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class DriverEngagementCache {

    private static final String SHIFT_KEY_PREFIX = "driver:shift:";
    private static final String ACTIVE_ORDER_KEY_PREFIX = "driver:active-order:";
    private static final Duration TTL = Duration.ofMinutes(5);

    private final StringRedisTemplate redisTemplate;

    public Map<Long, Boolean> getShiftFlags(Collection<Long> driverIds) {
        return fetchFlags(driverIds, SHIFT_KEY_PREFIX);
    }

    public Map<Long, Boolean> getActiveOrderFlags(Collection<Long> driverIds) {
        return fetchFlags(driverIds, ACTIVE_ORDER_KEY_PREFIX);
    }

    public void storeShiftFlags(Map<Long, Boolean> flags) {
        storeFlags(flags, SHIFT_KEY_PREFIX);
    }

    public void storeActiveOrderFlags(Map<Long, Boolean> flags) {
        storeFlags(flags, ACTIVE_ORDER_KEY_PREFIX);
    }

    private Map<Long, Boolean> fetchFlags(Collection<Long> driverIds, String prefix) {
        Map<Long, Boolean> result = new HashMap<>();
        if (driverIds.isEmpty()) {
            return result;
        }

        List<String> keys = driverIds.stream().map(id -> prefix + id).toList();
        List<String> values = redisTemplate.opsForValue().multiGet(keys);
        if (values == null) {
            return result;
        }

        int index = 0;
        for (Long driverId : driverIds) {
            String value = values.get(index++);
            if (value != null) {
                result.put(driverId, Boolean.parseBoolean(value));
            }
        }
        return result;
    }

    private void storeFlags(Map<Long, Boolean> flags, String prefix) {
        if (flags.isEmpty()) {
            return;
        }
        flags.forEach((driverId, value) ->
                redisTemplate.opsForValue().set(prefix + driverId, Boolean.toString(value), TTL)
        );
    }
}
