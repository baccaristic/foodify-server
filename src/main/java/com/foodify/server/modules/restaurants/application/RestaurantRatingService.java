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
        if (request == null || request.thumbsUp() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A thumbs up/down value is required");
        }

        RestaurantRating rating = restaurantRatingRepository.findByOrder_Id(orderId)
                .orElseGet(RestaurantRating::new);

        Restaurant restaurant = order.getRestaurant();
        rating.setRestaurant(restaurant);
        rating.setOrder(order);
        rating.setClient(order.getClient());
        rating.setThumbsUp(request.thumbsUp());
        rating.setComments(StringUtils.hasText(request.comments()) ? request.comments().trim() : null);

        RestaurantRating saved = restaurantRatingRepository.save(rating);

        RatingAggregate aggregate = restaurantRatingRepository.findAggregateByRestaurantId(restaurant.getId());
        long totalCount = aggregate != null ? aggregate.totalCount() : 0L;
        long thumbsUpCount = aggregate != null ? aggregate.thumbsUpCount() : 0L;
        long thumbsDownCount = Math.max(totalCount - thumbsUpCount, 0L);

        Double restaurantAverage = totalCount > 0
                ? roundToOneDecimal(((double) thumbsUpCount / totalCount) * 5)
                : null;
        restaurant.setRating(restaurantAverage);

        return new RestaurantRatingResponse(
                saved.getId(),
                restaurant.getId(),
                order.getId(),
                order.getClient().getId(),
                saved.getThumbsUp(),
                saved.getComments(),
                restaurantAverage,
                totalCount,
                thumbsUpCount,
                thumbsDownCount,
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
                    long totalCount = aggregate != null ? aggregate.totalCount() : 0L;
                    long thumbsUpCount = aggregate != null ? aggregate.thumbsUpCount() : 0L;
                    long thumbsDownCount = Math.max(totalCount - thumbsUpCount, 0L);
                    Double restaurantAverage = totalCount > 0
                            ? roundToOneDecimal(((double) thumbsUpCount / totalCount) * 5)
                            : null;
                    return new RestaurantRatingResponse(
                            rating.getId(),
                            order.getRestaurant().getId(),
                            order.getId(),
                            order.getClient().getId(),
                            rating.getThumbsUp(),
                            rating.getComments(),
                            restaurantAverage,
                            totalCount,
                            thumbsUpCount,
                            thumbsDownCount,
                            rating.getCreatedAt(),
                            rating.getUpdatedAt()
                    );
                });
    }

    private double roundToOneDecimal(double value) {
        BigDecimal decimal = BigDecimal.valueOf(value);
        return decimal.setScale(1, RoundingMode.HALF_UP).doubleValue();
    }
}
