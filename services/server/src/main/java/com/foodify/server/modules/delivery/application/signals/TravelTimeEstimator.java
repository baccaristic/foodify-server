package com.foodify.server.modules.delivery.application.signals;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TravelTimeEstimator {

    private static final double DEFAULT_SPEED_KMPH = 28.0;

    private final TrafficConditionsService trafficConditionsService;

    public double estimateEtaMinutes(Long driverId, double distanceKm) {
        if (distanceKm <= 0) {
            return 0;
        }
        double effectiveSpeed = DEFAULT_SPEED_KMPH / trafficConditionsService.resolveMultiplier(driverId);
        if (effectiveSpeed <= 0) {
            effectiveSpeed = DEFAULT_SPEED_KMPH;
        }
        double hours = distanceKm / effectiveSpeed;
        return hours * 60.0;
    }
}
