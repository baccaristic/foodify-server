package com.foodify.server.modules.delivery.location;

import com.foodify.server.modules.identity.domain.Driver;
import com.foodify.server.modules.identity.repository.DriverRepository;
import io.micrometer.core.annotation.Timed;
import lombok.RequiredArgsConstructor;
import org.springframework.data.geo.*;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DriverLocationService {

    private final StringRedisTemplate redisTemplate;
    private static final String GEO_KEY = "drivers:geo";
    private static final String STATUS_KEY_PREFIX = "driver:status:";
    private final DriverRepository driverRepository;
    // ✅ Find closest available drivers
    public List<DriverCandidate> findClosestDrivers(double lat, double lon, double radiusKm, int limit) {
        GeoResults<RedisGeoCommands.GeoLocation<String>> results =
                redisTemplate.opsForGeo().radius(
                        GEO_KEY,
                        new Circle(new Point(lon, lat), new Distance(radiusKm, Metrics.KILOMETERS)),
                        RedisGeoCommands.GeoRadiusCommandArgs.newGeoRadiusArgs().limit(limit).sortAscending()
                );

        if (results == null) {
            return List.of();
        }

        List<GeoResult<RedisGeoCommands.GeoLocation<String>>> content = results.getContent();
        if (content.isEmpty()) {
            return List.of();
        }

        List<String> driverIds = content.stream()
                .map(result -> result.getContent().getName())
                .toList();
        Map<String, Boolean> availability = fetchAvailability(driverIds);

        return content.stream()
                .map(r -> {
                    Distance distance = r.getDistance();
                    double distanceKmValue = distance != null ? distance.getValue() : Double.MAX_VALUE;
                    return new DriverCandidate(r.getContent().getName(), distanceKmValue);
                })
                .filter(candidate -> availability.getOrDefault(candidate.driverId(), true))
                .collect(Collectors.toList());
    }

    public record DriverCandidate(String driverId, double distanceKm) { }

    public boolean isAvailable(String driverId) {
        String status = redisTemplate.opsForValue().get(STATUS_KEY_PREFIX + driverId);
        return isAvailableStatus(status);
    }

    private Map<String, Boolean> fetchAvailability(List<String> driverIds) {
        if (driverIds.isEmpty()) {
            return Map.of();
        }

        List<String> keys = driverIds.stream()
                .map(id -> STATUS_KEY_PREFIX + id)
                .toList();
        List<String> statuses = redisTemplate.opsForValue().multiGet(keys);

        Map<String, Boolean> availability = new HashMap<>();
        if (statuses == null) {
            driverIds.forEach(id -> availability.put(id, true));
            return availability;
        }

        for (int i = 0; i < driverIds.size(); i++) {
            String status = i < statuses.size() ? statuses.get(i) : null;
            availability.put(driverIds.get(i), isAvailableStatus(status));
        }

        return availability;
    }

    private boolean isAvailableStatus(String status) {
        return status == null || status.equals("AVAILABLE");
    }

    public void markPending(String driverId, Long orderId) {
        redisTemplate.opsForValue().set(STATUS_KEY_PREFIX + driverId, "PENDING:" + orderId);
    }

    public void markAvailable(String driverId) {
        Driver driver = driverRepository.findById(Long.valueOf(driverId)).orElse(null);
        if (driver == null) {
            return;
        }

        driver.setAvailable(true);
        driverRepository.save(driver);
        redisTemplate.opsForValue().set(STATUS_KEY_PREFIX + driverId, "AVAILABLE");
    }

    public void markUnavailable(String driverId) {
        Driver driver = driverRepository.findById(Long.valueOf(driverId)).orElse(null);
        if (driver == null) {
            return;
        }

        driver.setAvailable(false);
        driverRepository.save(driver);
        redisTemplate.opsForValue().set(STATUS_KEY_PREFIX + driverId, "UNAVAILABLE");
        removeDriver(Long.valueOf(driverId));
    }

    public Point getLastKnownPosition(Long driverId) {
        List<Point> positions = redisTemplate.opsForGeo()
                .position(GEO_KEY, driverId.toString());

        if (positions == null || positions.isEmpty() || positions.get(0) == null) {
            return null; // no known location
        }

        return positions.get(0); // Redis stores as (longitude, latitude)
    }

    public void markBusy(String driverId, Long orderId) {
        redisTemplate.opsForValue().set(STATUS_KEY_PREFIX + driverId, "BUSY:" + orderId);
    }
    @Timed(value = "drivers.location.update", description = "Time spent processing driver location updates", histogram = true)
    public void updateDriverLocation(Long driverId, double lat, double lng) {
        redisTemplate.opsForGeo().add(GEO_KEY, new Point(lng, lat), driverId.toString());
        ensureAvailabilityKeyInitialized(driverId);
    }

    private void ensureAvailabilityKeyInitialized(Long driverId) {
        String redisKey = STATUS_KEY_PREFIX + driverId;
        redisTemplate.opsForValue().setIfAbsent(redisKey, "AVAILABLE");
    }
    public void removeDriver(Long driverId) {
        redisTemplate.opsForGeo().remove(GEO_KEY, driverId.toString());
    }

}


