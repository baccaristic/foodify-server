package com.foodify.server.modules.restaurants.application;

import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class DeliveryFeeCalculator {

    private static final double EARTH_RADIUS_KM = 6371.0;
    private static final double COST_PER_KM = 0.5;

    public Optional<Double> calculateFee(Double clientLatitude, Double clientLongitude,
                                         Double restaurantLatitude, Double restaurantLongitude) {
        return calculateDistance(clientLatitude, clientLongitude, restaurantLatitude, restaurantLongitude)
                .map(distance -> {
                    long roundedKilometers = Math.max(1, Math.round(distance));
                    double fee = roundedKilometers * COST_PER_KM;
                    return fee;
                });
    }

    public Optional<Double> calculateDistance(Double clientLatitude, Double clientLongitude,
                                              Double restaurantLatitude, Double restaurantLongitude) {
        if (clientLatitude == null || clientLongitude == null
                || restaurantLatitude == null || restaurantLongitude == null) {
            return Optional.empty();
        }
        double distance = haversine(clientLatitude, clientLongitude, restaurantLatitude, restaurantLongitude);
        return Optional.of(distance);
    }

    private double haversine(double lat1, double lon1, double lat2, double lon2) {
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return EARTH_RADIUS_KM * c;
    }
}
