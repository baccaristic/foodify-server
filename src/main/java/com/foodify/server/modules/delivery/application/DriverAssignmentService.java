package com.foodify.server.modules.delivery.application;

import com.foodify.server.modules.delivery.application.DriverFinancialService;
import com.foodify.server.modules.delivery.application.signals.DriverCapacityService;
import com.foodify.server.modules.delivery.application.signals.DriverEngagementCache;
import com.foodify.server.modules.delivery.application.signals.TravelTimeEstimator;
import com.foodify.server.modules.delivery.config.DriverAssignmentProperties;
import com.foodify.server.modules.delivery.domain.Delivery;
import com.foodify.server.modules.delivery.domain.DriverShift;
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
import java.util.Collection;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
@Slf4j
public class DriverAssignmentService {

    private static final EnumSet<OrderStatus> ACTIVE_DRIVER_STATUSES = EnumSet.of(
            OrderStatus.ACCEPTED,
            OrderStatus.PREPARING,
            OrderStatus.READY_FOR_PICK_UP,
            OrderStatus.IN_DELIVERY
    );

    private final DriverAssignmentProperties properties;
    private final DriverLocationService driverLocationService;
    private final DriverRepository driverRepository;
    private final DriverShiftRepository driverShiftRepository;
    private final OrderRepository orderRepository;
    private final DeliveryRepository deliveryRepository;
    private final TravelTimeEstimator travelTimeEstimator;
    private final DriverCapacityService driverCapacityService;
    private final DriverEngagementCache driverEngagementCache;
    private final DriverFinancialService driverFinancialService;

    public Optional<DriverMatch> findBestDriver(Order order, Set<Long> deprioritizedDriverIds) {
        if (order.getRestaurant() == null) {
            log.debug("Skipping driver assignment for order {} because the restaurant is missing", order.getId());
            return Optional.empty();
        }

        double lat = order.getRestaurant().getLatitude();
        double lon = order.getRestaurant().getLongitude();

        Set<Long> deprioritized = deprioritizedDriverIds != null ? deprioritizedDriverIds : Set.of();
        Set<Long> evaluatedDrivers = new HashSet<>();
        DriverMatch bestMatch = null;

        for (double radius : properties.searchRadiiKm()) {
            RadiusStats stats = new RadiusStats(radius);

            List<DriverLocationService.DriverCandidate> candidates = driverLocationService
                    .findClosestDrivers(lat, lon, radius, properties.candidateLimit());
            stats.recordCandidates(candidates.size());

            LinkedHashMap<Long, Double> candidateDistances = new LinkedHashMap<>();
            LinkedHashMap<Long, Double> deprioritizedDistances = new LinkedHashMap<>();
            for (DriverLocationService.DriverCandidate candidate : candidates) {
                Long driverId = parseDriverId(candidate.driverId());
                if (driverId == null) {
                    stats.incrementInvalidIdentifiers();
                    continue;
                }
                if (!evaluatedDrivers.add(driverId)) {
                    stats.incrementDuplicateCandidates();
                    continue;
                }
                if (deprioritized.contains(driverId)) {
                    stats.incrementDeprioritizedDrivers();
                    deprioritizedDistances.put(driverId, candidate.distanceKm());
                } else {
                    candidateDistances.put(driverId, candidate.distanceKm());
                }
            }

            candidateDistances.putAll(deprioritizedDistances);

            if (candidateDistances.isEmpty()) {
                stats.logSummary();
                continue;
            }

            Map<Long, Driver> fetchedDrivers = StreamSupport
                    .stream(driverRepository.findAllById(candidateDistances.keySet()).spliterator(), false)
                    .collect(Collectors.toMap(Driver::getId, driver -> driver));

            Map<Long, Driver> availableDrivers = new HashMap<>();
            for (Long driverId : candidateDistances.keySet()) {
                Driver driver = fetchedDrivers.get(driverId);
                if (driver == null) {
                    stats.incrementMissingDrivers();
                    continue;
                }
                if (!driver.isAvailable()) {
                    stats.incrementUnavailableDrivers();
                    continue;
                }
                availableDrivers.put(driverId, driver);
            }

            if (availableDrivers.isEmpty()) {
                stats.logSummary();
                continue;
            }

            Map<Long, Boolean> cachedShiftFlags = driverEngagementCache.getShiftFlags(availableDrivers.keySet());
            Set<Long> shiftLookupIds = availableDrivers.keySet().stream()
                    .filter(id -> !Boolean.FALSE.equals(cachedShiftFlags.get(id)))
                    .collect(Collectors.toCollection(LinkedHashSet::new));

            Map<Long, DriverShift> activeShifts = new HashMap<>();
            if (!shiftLookupIds.isEmpty()) {
                driverShiftRepository.findAllByDriverIdInAndStatusOrderByStartedAtDesc(shiftLookupIds, DriverShiftStatus.ACTIVE)
                        .forEach(shift -> activeShifts.putIfAbsent(shift.getDriver().getId(), shift));
            }

            Map<Long, Boolean> activeShiftFlags = new HashMap<>();
            for (Long driverId : availableDrivers.keySet()) {
                if (Boolean.FALSE.equals(cachedShiftFlags.get(driverId))) {
                    activeShiftFlags.put(driverId, false);
                } else {
                    activeShiftFlags.put(driverId, activeShifts.containsKey(driverId));
                }
            }
            driverEngagementCache.storeShiftFlags(activeShiftFlags);

            Map<Long, Boolean> cachedActiveOrders = driverEngagementCache.getActiveOrderFlags(availableDrivers.keySet());
            Set<Long> orderLookupIds = availableDrivers.keySet().stream()
                    .filter(id -> !Boolean.FALSE.equals(cachedActiveOrders.get(id)))
                    .collect(Collectors.toSet());
            Set<Long> activeOrderDriverIds = orderLookupIds.isEmpty() ? Set.of() :
                    orderRepository.findDriverIdsByStatusIn(orderLookupIds, ACTIVE_DRIVER_STATUSES);

            Map<Long, Boolean> activeOrders = new HashMap<>();
            for (Long driverId : availableDrivers.keySet()) {
                boolean hasActiveOrder = activeOrderDriverIds.contains(driverId) || Boolean.TRUE.equals(cachedActiveOrders.get(driverId));
                activeOrders.put(driverId, hasActiveOrder);
            }
            driverEngagementCache.storeActiveOrderFlags(activeOrders);

            LinkedHashSet<Long> eligibleDriverIds = new LinkedHashSet<>();
            for (Long driverId : candidateDistances.keySet()) {
                if (!availableDrivers.containsKey(driverId)) {
                    continue;
                }
                if (!activeShiftFlags.getOrDefault(driverId, false)) {
                    stats.incrementNoActiveShift();
                    continue;
                }
                if (activeOrders.getOrDefault(driverId, false)) {
                    stats.incrementActiveDeliveries();
                    continue;
                }
                Driver driver = availableDrivers.get(driverId);
                if (driverFinancialService.isDepositRequired(driver) && 
                    driverFinancialService.hasDepositDeadlinePassed(driver)) {
                    stats.incrementDepositRestricted();
                    continue;
                }
                eligibleDriverIds.add(driverId);
            }

            if (eligibleDriverIds.isEmpty()) {
                stats.logSummary();
                continue;
            }

            Map<Long, Delivery> recentDeliveries = loadRecentDeliveries(eligibleDriverIds);

            DriverMatch bestMatchInRadius = null;
            for (Long driverId : eligibleDriverIds) {
                Driver driver = availableDrivers.get(driverId);
                double distanceKm = candidateDistances.get(driverId);
                Duration idleDuration = resolveIdleDuration(driverId, recentDeliveries, activeShifts);
                double etaMinutes = travelTimeEstimator.estimateEtaMinutes(driverId, distanceKm);
                double capacityFactor = driverCapacityService.resolveCapacityFactor(driverId);
                double score = computeScore(distanceKm, idleDuration, etaMinutes, capacityFactor);

                DriverMatch match = new DriverMatch(driver, score, distanceKm, idleDuration, etaMinutes, capacityFactor);
                log.debug("Driver {} scored {} for assignment (distance={}km idle={}min eta={}min capacity={})",
                        driver.getId(), String.format("%.3f", score),
                        String.format("%.2f", distanceKm), idleDuration.toMinutes(),
                        String.format("%.1f", etaMinutes), String.format("%.2f", capacityFactor));

                if (bestMatchInRadius == null || match.score() > bestMatchInRadius.score()) {
                    bestMatchInRadius = match;
                }

                if (match.score() >= properties.earlyExitScoreThreshold() ||
                        distanceKm <= properties.earlyExitDistanceKm()) {
                    log.debug("Early exit triggered for driver {} within radius {} km (score={}, distance={})",
                            driver.getId(), radius, String.format("%.3f", match.score()), String.format("%.2f", distanceKm));
                    return Optional.of(match);
                }
            }

            if (bestMatchInRadius != null) {
                bestMatch = bestMatchInRadius;
                log.debug("Selected driver {} from radius {} km after evaluating {} eligible candidates",
                        bestMatch.driver().getId(), radius, eligibleDriverIds.size());
                break;
            }

            stats.logSummary();
        }

        return Optional.ofNullable(bestMatch);
    }

    private Map<Long, Delivery> loadRecentDeliveries(Collection<Long> driverIds) {
        if (driverIds.isEmpty()) {
            return Map.of();
        }
        List<Delivery> deliveries = deliveryRepository.findRecentForDrivers(driverIds);
        Map<Long, Delivery> result = new HashMap<>();
        for (Delivery delivery : deliveries) {
            if (delivery.getDriver() == null) {
                continue;
            }
            result.putIfAbsent(delivery.getDriver().getId(), delivery);
        }
        return result;
    }

    private Duration resolveIdleDuration(Long driverId, Map<Long, Delivery> recentDeliveries, Map<Long, DriverShift> activeShifts) {
        LocalDateTime now = LocalDateTime.now();
        Delivery recentDelivery = recentDeliveries.get(driverId);
        if (recentDelivery != null) {
            LocalDateTime lastActivity = Optional.ofNullable(recentDelivery.getDeliveredTime())
                    .or(() -> Optional.ofNullable(recentDelivery.getPickupTime()))
                    .orElse(recentDelivery.getAssignedTime());
            if (lastActivity != null) {
                Duration duration = Duration.between(lastActivity, now);
                if (!duration.isNegative()) {
                    return duration;
                }
            }
        }

        DriverShift shift = activeShifts.get(driverId);
        if (shift != null && shift.getStartedAt() != null) {
            Duration sinceShiftStart = Duration.between(shift.getStartedAt(), now);
            if (!sinceShiftStart.isNegative()) {
                return sinceShiftStart;
            }
        }

        long fallbackMinutes = Math.max(1, Math.round(properties.maxIdleBonusMinutes() / 2));
        return Duration.ofMinutes(fallbackMinutes);
    }

    private double computeScore(double distanceKm, Duration idleDuration, double etaMinutes, double capacityFactor) {
        double totalWeight = properties.distanceWeight() + properties.idleWeight()
                + properties.etaWeight() + properties.capacityWeight();
        if (totalWeight <= 0) {
            totalWeight = 1.0;
        }

        double normalizedDistance = 1.0 / (1.0 + Math.max(distanceKm, 0.0));
        double idleMinutes = Math.max(0.0, Math.min(properties.maxIdleBonusMinutes(), idleDuration.toMinutes()));
        double idleScore = idleMinutes / properties.maxIdleBonusMinutes();
        double etaRatio = Math.max(0.0, etaMinutes) / Math.max(1.0, properties.targetEtaMinutes());
        double etaScore = 1.0 / (1.0 + etaRatio);
        double capacityScore = Math.max(0.0, Math.min(1.0, capacityFactor));

        return (normalizedDistance * properties.distanceWeight()
                + idleScore * properties.idleWeight()
                + etaScore * properties.etaWeight()
                + capacityScore * properties.capacityWeight()) / totalWeight;
    }

    private Long parseDriverId(String driverIdValue) {
        try {
            return Long.valueOf(driverIdValue);
        } catch (NumberFormatException ex) {
            log.warn("Unable to parse driver id {} from geo results", driverIdValue, ex);
            return null;
        }
    }

    private class RadiusStats {
        private final double radiusKm;
        private int candidateCount;
        private int invalidIdentifiers;
        private int deprioritizedDrivers;
        private int duplicateCandidates;
        private int missingDrivers;
        private int unavailableDrivers;
        private int noActiveShift;
        private int activeDeliveries;
        private int depositRestricted;

        private RadiusStats(double radiusKm) {
            this.radiusKm = radiusKm;
        }

        void recordCandidates(int count) {
            this.candidateCount = count;
        }

        void incrementInvalidIdentifiers() {
            this.invalidIdentifiers++;
        }

        void incrementDeprioritizedDrivers() {
            this.deprioritizedDrivers++;
        }

        void incrementDuplicateCandidates() {
            this.duplicateCandidates++;
        }

        void incrementMissingDrivers() {
            this.missingDrivers++;
        }

        void incrementUnavailableDrivers() {
            this.unavailableDrivers++;
        }

        void incrementNoActiveShift() {
            this.noActiveShift++;
        }

        void incrementActiveDeliveries() {
            this.activeDeliveries++;
        }

        void incrementDepositRestricted() {
            this.depositRestricted++;
        }

        void logSummary() {
            log.info("Radius {}km produced no matches (candidates={}, invalidIds={}, deprioritized={}, duplicates={}, missing={}, unavailable={}, noShift={}, activeDeliveries={}, depositRestricted={})",
                    radiusKm, candidateCount, invalidIdentifiers, deprioritizedDrivers, duplicateCandidates,
                    missingDrivers, unavailableDrivers, noActiveShift, activeDeliveries, depositRestricted);
        }
    }

    public record DriverMatch(Driver driver, double score, double distanceKm, Duration idleDuration,
                              double etaMinutes, double capacityFactor) { }
}
