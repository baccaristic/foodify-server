package com.foodify.server.modules.restaurants.repository;

import com.foodify.server.modules.identity.domain.RestaurantAdmin;
import com.foodify.server.modules.restaurants.domain.Restaurant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long>, JpaSpecificationExecutor<Restaurant> {

    String DISTANCE_FORMULA = """
            6371 * acos(
                cos(radians(:lat)) *
                cos(radians(r.latitude)) *
                cos(radians(r.longitude) - radians(:lng)) +
                sin(radians(:lat)) *
                sin(radians(r.latitude))
            )
            """;

    @Query(
            value = "SELECT r.* FROM restaurant r WHERE (" + DISTANCE_FORMULA + ") < :radius ORDER BY " + DISTANCE_FORMULA,
            countQuery = "SELECT COUNT(*) FROM restaurant r WHERE (" + DISTANCE_FORMULA + ") < :radius",
            nativeQuery = true
    )
    Page<Restaurant> findNearby(
            @Param("lat") double lat,
            @Param("lng") double lng,
            @Param("radius") double radiusInKm,
            Pageable pageable
    );

    @Query(
            value = "SELECT r.* FROM restaurant r WHERE (" + DISTANCE_FORMULA + ") < :radius AND r.top_choice = TRUE ORDER BY " + DISTANCE_FORMULA,
            countQuery = "SELECT COUNT(*) FROM restaurant r WHERE (" + DISTANCE_FORMULA + ") < :radius AND r.top_choice = TRUE",
            nativeQuery = true
    )
    Page<Restaurant> findTopChoiceNearby(
            @Param("lat") double lat,
            @Param("lng") double lng,
            @Param("radius") double radiusInKm,
            Pageable pageable
    );

    @Query(
            value = "SELECT r.* FROM restaurant r WHERE (" + DISTANCE_FORMULA + ") < :radius AND r.free_delivery = TRUE ORDER BY " + DISTANCE_FORMULA,
            countQuery = "SELECT COUNT(*) FROM restaurant r WHERE (" + DISTANCE_FORMULA + ") < :radius AND r.free_delivery = TRUE",
            nativeQuery = true
    )
    Page<Restaurant> findNearbyWithPromotions(
            @Param("lat") double lat,
            @Param("lng") double lng,
            @Param("radius") double radiusInKm,
            Pageable pageable
    );

    @Query(
            value = "SELECT r.* FROM restaurant r WHERE (" + DISTANCE_FORMULA + ") < :radius " +
                    "AND EXISTS (" +
                    "SELECT 1 FROM menu_category mc WHERE mc.restaurant_id = r.id AND LOWER(mc.name) = LOWER(:category)) " +
                    "ORDER BY " + DISTANCE_FORMULA,
            countQuery = "SELECT COUNT(*) FROM restaurant r WHERE (" + DISTANCE_FORMULA + ") < :radius " +
                    "AND EXISTS (" +
                    "SELECT 1 FROM menu_category mc WHERE mc.restaurant_id = r.id AND LOWER(mc.name) = LOWER(:category))",
            nativeQuery = true
    )
    Page<Restaurant> findNearbyByCategory(
            @Param("lat") double lat,
            @Param("lng") double lng,
            @Param("radius") double radiusInKm,
            @Param("category") String category,
            Pageable pageable
    );

    @Query(
            value = "SELECT r.* FROM restaurant r WHERE (" + DISTANCE_FORMULA + ") < :radius AND r.id NOT IN (:excludedIds) ORDER BY " + DISTANCE_FORMULA,
            countQuery = "SELECT COUNT(*) FROM restaurant r WHERE (" + DISTANCE_FORMULA + ") < :radius AND r.id NOT IN (:excludedIds)",
            nativeQuery = true
    )
    Page<Restaurant> findNearbyExcluding(
            @Param("lat") double lat,
            @Param("lng") double lng,
            @Param("radius") double radiusInKm,
            @Param("excludedIds") Iterable<Long> excludedIds,
            Pageable pageable
    );

    Restaurant getRestaurantByAdmin(RestaurantAdmin admin);
}
