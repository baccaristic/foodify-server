package com.foodify.server.modules.restaurants.repository;

import com.foodify.server.modules.restaurants.domain.RestaurantRating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface RestaurantRatingRepository extends JpaRepository<RestaurantRating, Long> {

    Optional<RestaurantRating> findByOrder_Id(Long orderId);

    @Query("""
            SELECT new com.foodify.server.modules.restaurants.repository.RestaurantRatingRepository$RatingAggregate(
                COUNT(r),
                COALESCE(SUM(CASE WHEN r.thumbsUp = true THEN 1 ELSE 0 END), 0)
            )
            FROM RestaurantRating r
            WHERE r.restaurant.id = :restaurantId
            """)
    RatingAggregate findAggregateByRestaurantId(@Param("restaurantId") Long restaurantId);

    record RatingAggregate(long totalCount, long thumbsUpCount) {
    }
}
