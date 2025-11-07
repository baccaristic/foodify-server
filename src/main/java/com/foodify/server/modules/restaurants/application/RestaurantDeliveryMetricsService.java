package com.foodify.server.modules.restaurants.application;

import com.foodify.server.modules.orders.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RestaurantDeliveryMetricsService {
    
    private final OrderRepository orderRepository;
    
    // Average speed in km/h for delivery (conservative estimate)
    private static final double AVERAGE_DELIVERY_SPEED_KMH = 20.0;
    // Average restaurant preparation time in minutes
    private static final int DEFAULT_PREPARATION_TIME_MINUTES = 25;
    // Buffer time in minutes for assignment and pickup
    private static final int BUFFER_TIME_MINUTES = 5;
    
    /**
     * Calculate estimated delivery time based on distance and historical data
     * @param restaurantId the restaurant ID
     * @param distanceKm distance from client to restaurant in kilometers
     * @return estimated delivery time in minutes
     */
    public Integer calculateEstimatedDeliveryTime(Long restaurantId, double distanceKm) {
        if (restaurantId == null || distanceKm < 0) {
            return null;
        }
        
        // Get historical average preparation time for this restaurant, or use default
        Integer avgPrepTime = getAveragePreparationTime(restaurantId);
        if (avgPrepTime == null) {
            avgPrepTime = DEFAULT_PREPARATION_TIME_MINUTES;
        }
        
        // Calculate travel time based on distance
        double travelTimeMinutes = (distanceKm / AVERAGE_DELIVERY_SPEED_KMH) * 60.0;
        
        // Total time = preparation + travel + buffer
        return (int) Math.round(avgPrepTime + travelTimeMinutes + BUFFER_TIME_MINUTES);
    }
    
    /**
     * Get average preparation time for a restaurant based on historical orders
     * @param restaurantId the restaurant ID
     * @return average preparation time in minutes, or null if no data
     */
    private Integer getAveragePreparationTime(Long restaurantId) {
        List<OrderStats> stats = orderRepository.getRestaurantOrderStats(restaurantId);
        
        if (stats.isEmpty()) {
            return null;
        }
        
        Double avgPrepMinutes = stats.get(0).getAvgPreparationMinutes();
        return avgPrepMinutes != null ? avgPrepMinutes.intValue() : null;
    }
    
    /**
     * Interface for order statistics projection
     */
    public interface OrderStats {
        Long getRestaurantId();
        Long getTotalOrders();
        Long getCompletedOrders();
        Double getAvgDeliveryMinutes();
        Double getAvgPreparationMinutes();
    }
}
