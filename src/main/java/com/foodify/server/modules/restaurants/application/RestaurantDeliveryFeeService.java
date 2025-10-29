package com.foodify.server.modules.restaurants.application;

import com.foodify.server.modules.restaurants.domain.Restaurant;
import com.foodify.server.modules.restaurants.dto.DeliveryFeeResponse;
import com.foodify.server.modules.restaurants.repository.RestaurantRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RestaurantDeliveryFeeService {

    private static final double MAX_DELIVERY_DISTANCE_KM = 10.0;

    private final RestaurantRepository restaurantRepository;
    private final DeliveryFeeCalculator deliveryFeeCalculator;

    public DeliveryFeeResponse calculateDeliveryFee(Long restaurantId, Double clientLatitude, Double clientLongitude) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new EntityNotFoundException("Restaurant not found"));

        return deliveryFeeCalculator.calculateDistance(
                        clientLatitude,
                        clientLongitude,
                        restaurant.getLatitude(),
                        restaurant.getLongitude()
                )
                .map(distance -> {
                    if (distance > MAX_DELIVERY_DISTANCE_KM) {
                        return new DeliveryFeeResponse(false, distance, null);
                    }
                    Double fee = deliveryFeeCalculator.calculateFee(
                                    clientLatitude,
                                    clientLongitude,
                                    restaurant.getLatitude(),
                                    restaurant.getLongitude()
                            )
                            .orElse(null);
                    return new DeliveryFeeResponse(true, distance, fee);
                })
                .orElseGet(() -> new DeliveryFeeResponse(false, null, null));
    }
}
