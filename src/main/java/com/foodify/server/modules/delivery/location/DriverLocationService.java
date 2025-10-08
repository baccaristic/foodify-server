package com.foodify.server.modules.delivery.location;

import com.foodify.server.modules.identity.domain.Driver;
import com.foodify.server.modules.identity.repository.DriverRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.geo.*;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
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

        return results.getContent().stream()
                .map(r -> {
                    Distance distance = r.getDistance();
                    double distanceKmValue = distance != null ? distance.getValue() : Double.MAX_VALUE;
                    return new DriverCandidate(r.getContent().getName(), distanceKmValue);
                })
                .filter(candidate -> isAvailable(candidate.driverId())) // ✅ only pick available drivers
                .collect(Collectors.toList());
    }

    public record DriverCandidate(String driverId, double distanceKm) { }

    public boolean isAvailable(String driverId) {
        String status = redisTemplate.opsForValue().get(STATUS_KEY_PREFIX + driverId);
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
    public void updateDriverLocation(Long driverId, double lat, double lng) {
        Driver driver = this.driverRepository.findById(driverId).orElse(null);
        if (driver == null) return;
        if (driver.isAvailable()) {
            this.markAvailable(String.valueOf(driverId));
        }
        redisTemplate.opsForGeo().add(GEO_KEY, new Point(lng, lat), driverId.toString());
    }
    public void removeDriver(Long driverId) {
        redisTemplate.opsForGeo().remove(GEO_KEY, driverId.toString());
    }

}


