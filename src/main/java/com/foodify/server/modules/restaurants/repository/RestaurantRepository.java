package com.foodify.server.modules.restaurants.repository;

import com.foodify.server.modules.identity.domain.Admin;
import com.foodify.server.modules.restaurants.domain.Restaurant;
import com.foodify.server.modules.identity.domain.RestaurantAdmin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long>, JpaSpecificationExecutor<Restaurant> {
    @Query(value = """

            SELECT * FROM (
          SELECT r.*, (
              6371 * acos(
                  cos(radians(:lat)) *
                  cos(radians(r.latitude)) *
                  cos(radians(r.longitude) - radians(:lng)) +
                  sin(radians(:lat)) *
                  sin(radians(r.latitude))
              )
          ) AS distance
          FROM restaurant r
        ) as sub
        WHERE distance < :radius
        ORDER BY distance
        """, nativeQuery = true)
    List<Restaurant> findNearby(@Param("lat") double lat, @Param("lng") double lng, @Param("radius") double radiusInKm);

    Restaurant getRestaurantByAdmin(RestaurantAdmin admin);
}
