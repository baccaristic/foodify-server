package com.foodify.server.modules.restaurants.application;

import com.foodify.server.modules.orders.domain.Order;
import com.foodify.server.modules.orders.domain.OrderStatus;
import com.foodify.server.modules.orders.repository.OrderRepository;
import com.foodify.server.modules.restaurants.domain.Restaurant;
import com.foodify.server.modules.restaurants.domain.RestaurantRating;
import com.foodify.server.modules.restaurants.dto.RestaurantRatingRequest;
import com.foodify.server.modules.restaurants.dto.RestaurantRatingResponse;
import com.foodify.server.modules.restaurants.repository.RestaurantRatingRepository;
import com.foodify.server.modules.restaurants.repository.RestaurantRatingRepository.RatingAggregate;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RestaurantRatingService {

    private final RestaurantRatingRepository restaurantRatingRepository;
    private final OrderRepository orderRepository;

    @Transactional
    public RestaurantRatingResponse rateRestaurant(Long clientId, Long orderId, RestaurantRatingRequest request) {
        if (clientId == null) {
            throw new IllegalArgumentException("Client id is required");
        }
        if (orderId == null) {
            throw new IllegalArgumentException("Order id is required");
        }
        Order order = orderRepository.findDetailedById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));
        if (order.getClient() == null || !order.getClient().getId().equals(clientId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You can only rate your own orders");
        }
        if (order.getStatus() != OrderStatus.DELIVERED) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Only delivered orders can be rated");
        }
        if (request == null || request.rating() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A rating value is required");
        }
        validateRating(request.rating());

        RestaurantRating rating = restaurantRatingRepository.findByOrder_Id(orderId)
                .orElseGet(RestaurantRating::new);

        Restaurant restaurant = order.getRestaurant();
        rating.setRestaurant(restaurant);
        rating.setOrder(order);
        rating.setClient(order.getClient());
        rating.setRating(request.rating());
        rating.setComments(StringUtils.hasText(request.comments()) ? request.comments().trim() : null);

        RestaurantRating saved = restaurantRatingRepository.save(rating);

        RatingAggregate aggregate = restaurantRatingRepository.findAggregateByRestaurantId(restaurant.getId());
        double average = aggregate != null ? aggregate.averageRating() : 0.0;
        long count = aggregate != null ? aggregate.ratingCount() : 0L;

        Double restaurantAverage = count > 0 ? roundToHalf(average) : null;
        restaurant.setRating(restaurantAverage);

        return new RestaurantRatingResponse(
                saved.getId(),
                restaurant.getId(),
                order.getId(),
                order.getClient().getId(),
                saved.getRating(),
                saved.getComments(),
                restaurantAverage,
                count,
                saved.getCreatedAt(),
                saved.getUpdatedAt()
        );
    }

    @Transactional
    public Optional<RestaurantRatingResponse> findRating(Long clientId, Long orderId) {
        if (clientId == null) {
            throw new IllegalArgumentException("Client id is required");
        }
        if (orderId == null) {
            throw new IllegalArgumentException("Order id is required");
        }
        Order order = orderRepository.findDetailedById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));
        if (order.getClient() == null || !order.getClient().getId().equals(clientId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You can only view your own ratings");
        }

        return restaurantRatingRepository.findByOrder_Id(orderId)
                .map(rating -> {
                    RatingAggregate aggregate = restaurantRatingRepository.findAggregateByRestaurantId(order.getRestaurant().getId());
                    double average = aggregate != null ? aggregate.averageRating() : 0.0;
                    long count = aggregate != null ? aggregate.ratingCount() : 0L;
                    Double restaurantAverage = count > 0 ? roundToHalf(average) : null;
                    return new RestaurantRatingResponse(
                            rating.getId(),
                            order.getRestaurant().getId(),
                            order.getId(),
                            order.getClient().getId(),
                            rating.getRating(),
                            rating.getComments(),
                            restaurantAverage,
                            count,
                            rating.getCreatedAt(),
                            rating.getUpdatedAt()
                    );
                });
    }

    private void validateRating(Integer value) {
        if (value < 1 || value > 5) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Rating must be between 1 and 5");
        }
    }

    private double roundToHalf(double value) {
        BigDecimal decimal = BigDecimal.valueOf(value);
        return decimal.setScale(1, RoundingMode.HALF_UP).doubleValue();
    }
}
