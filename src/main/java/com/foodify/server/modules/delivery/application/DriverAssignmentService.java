package com.foodify.server.modules.delivery.application;

import com.foodify.server.modules.delivery.domain.DriverShiftStatus;
import com.foodify.server.modules.delivery.location.DriverLocationService;
import com.foodify.server.modules.delivery.repository.DeliveryRepository;
import com.foodify.server.modules.delivery.repository.DriverShiftRepository;
import com.foodify.server.modules.identity.domain.Driver;
import com.foodify.server.modules.identity.repository.DriverRepository;
import com.foodify.server.modules.orders.domain.Order;
import com.foodify.server.modules.orders.domain.OrderStatus;
import com.foodify.server.modules.orders.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Encapsulates the driver matching heuristic used after a restaurant accepts an order.
 * The algorithm mimics the behaviour of modern delivery platforms by
 * <ul>
 *     <li>expanding the search radius in waves so nearby drivers are prioritised,</li>
 *     <li>filtering out drivers without an active shift or with ongoing deliveries, and</li>
 *     <li>scoring candidates by distance and idle time to balance speed with fairness.</li>
 * </ul>
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class DriverAssignmentService {

    private static final List<OrderStatus> ACTIVE_DRIVER_STATUSES = List.of(
            OrderStatus.ACCEPTED,
            OrderStatus.PREPARING,
            OrderStatus.READY_FOR_PICK_UP,
            OrderStatus.IN_DELIVERY
    );

    private static final double[] SEARCH_RADII_KM = {2, 4, 6, 8, 12, 18, 25};
    private static final int CANDIDATE_LIMIT_PER_RADIUS = 10;
    private static final double DISTANCE_WEIGHT = 0.7;
    private static final double IDLE_WEIGHT = 0.3;

    private final DriverLocationService driverLocationService;
    private final DriverRepository driverRepository;
    private final DriverShiftRepository driverShiftRepository;
    private final OrderRepository orderRepository;
    private final DeliveryRepository deliveryRepository;

    public Optional<DriverMatch> findBestDriver(Order order, Set<Long> excludedDriverIds) {
        if (order.getRestaurant() == null) {
            log.debug("Skipping driver assignment for order {} because the restaurant is missing", order.getId());
            return Optional.empty();
        }

        double lat = order.getRestaurant().getLatitude();
        double lon = order.getRestaurant().getLongitude();

        Set<Long> evaluatedDrivers = new HashSet<>(excludedDriverIds);
        List<DriverMatch> evaluatedMatches = new ArrayList<>();

        for (double radius : SEARCH_RADII_KM) {
            List<DriverLocationService.DriverCandidate> candidates = driverLocationService
                    .findClosestDrivers(lat, lon, radius, CANDIDATE_LIMIT_PER_RADIUS);

            for (DriverLocationService.DriverCandidate candidate : candidates) {
                Long driverId = parseDriverId(candidate.driverId());
                if (driverId == null || !evaluatedDrivers.add(driverId)) {
                    continue;
                }

                Optional<DriverMatch> match = evaluateCandidate(driverId, candidate.distanceKm());
                match.ifPresent(evaluatedMatches::add);
            }

            if (!evaluatedMatches.isEmpty()) {
                break; // we already found at least one candidate in this radius wave
            }
        }

        return evaluatedMatches.stream()
                .max((left, right) -> Double.compare(left.score(), right.score()));
    }

    private Optional<DriverMatch> evaluateCandidate(Long driverId, double distanceKm) {
        return driverRepository.findById(driverId)
                .filter(Driver::isAvailable)
                .filter(driver -> hasActiveShift(driverId))
                .filter(driver -> !hasActiveDelivery(driverId))
                .map(driver -> buildMatch(driver, distanceKm));
    }

    private DriverMatch buildMatch(Driver driver, double distanceKm) {
        Duration idleDuration = resolveIdleDuration(driver.getId());
        double score = computeScore(distanceKm, idleDuration);
        log.debug("Driver {} scored {} for assignment (distance={}km idle={}min)",
                driver.getId(), String.format("%.3f", score),
                String.format("%.2f", distanceKm), idleDuration.toMinutes());
        return new DriverMatch(driver, score, distanceKm, idleDuration);
    }

    private boolean hasActiveShift(Long driverId) {
        return driverShiftRepository
                .findTopByDriverIdAndStatusOrderByStartedAtDesc(driverId, DriverShiftStatus.ACTIVE)
                .isPresent();
    }

    private boolean hasActiveDelivery(Long driverId) {
        return orderRepository.findByDriverIdAndStatusIn(driverId, ACTIVE_DRIVER_STATUSES).isPresent();
    }

    private Duration resolveIdleDuration(Long driverId) {
        LocalDateTime now = LocalDateTime.now();
        return deliveryRepository
                .findTopByDriverIdOrderByDeliveredTimeDesc(driverId)
                .map(delivery -> Optional.ofNullable(delivery.getDeliveredTime())
                        .or(() -> Optional.ofNullable(delivery.getPickupTime()))
                        .orElse(delivery.getAssignedTime()))
                .map(lastActivity -> lastActivity != null ? Duration.between(lastActivity, now) : Duration.ZERO)
                .filter(duration -> !duration.isNegative())
                .orElse(Duration.ofHours(6));
    }

    private double computeScore(double distanceKm, Duration idleDuration) {
        double distanceScore = 1.0 / (1.0 + Math.max(distanceKm, 0));
        double idleScore = Math.min(idleDuration.toMinutes() / 30.0, 1.0);
        return (distanceScore * DISTANCE_WEIGHT) + (idleScore * IDLE_WEIGHT);
    }

    private Long parseDriverId(String driverIdValue) {
        try {
            return Long.valueOf(driverIdValue);
        } catch (NumberFormatException ex) {
            log.warn("Unable to parse driver id {} from geo results", driverIdValue, ex);
            return null;
        }
    }

    public record DriverMatch(Driver driver, double score, double distanceKm, Duration idleDuration) { }
}
