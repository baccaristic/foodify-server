package com.foodify.server.modules.restaurants.repository;

import com.foodify.server.modules.identity.domain.RestaurantAdmin;
import com.foodify.server.modules.restaurants.domain.Restaurant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;

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

    String BOUNDING_BOX_FILTER = "r.latitude BETWEEN :minLat AND :maxLat AND r.longitude BETWEEN :minLng AND :maxLng";

    @Query(
            value = "SELECT r.* FROM restaurant r WHERE " + BOUNDING_BOX_FILTER + " AND (" + DISTANCE_FORMULA + ") < :radius ORDER BY " + DISTANCE_FORMULA,
            countQuery = "SELECT COUNT(*) FROM restaurant r WHERE " + BOUNDING_BOX_FILTER + " AND (" + DISTANCE_FORMULA + ") < :radius",
            nativeQuery = true
    )
    Page<Restaurant> findNearby(
            @Param("lat") double lat,
            @Param("lng") double lng,
            @Param("radius") double radiusInKm,
            @Param("minLat") double minLat,
            @Param("maxLat") double maxLat,
            @Param("minLng") double minLng,
            @Param("maxLng") double maxLng,
            Pageable pageable
    );

    @Query(
            value = "SELECT r.* FROM restaurant r WHERE " + BOUNDING_BOX_FILTER + " AND (" + DISTANCE_FORMULA + ") < :radius AND r.top_choice = TRUE ORDER BY " + DISTANCE_FORMULA,
            countQuery = "SELECT COUNT(*) FROM restaurant r WHERE " + BOUNDING_BOX_FILTER + " AND (" + DISTANCE_FORMULA + ") < :radius AND r.top_choice = TRUE",
            nativeQuery = true
    )
    Page<Restaurant> findTopChoiceNearby(
            @Param("lat") double lat,
            @Param("lng") double lng,
            @Param("radius") double radiusInKm,
            @Param("minLat") double minLat,
            @Param("maxLat") double maxLat,
            @Param("minLng") double minLng,
            @Param("maxLng") double maxLng,
            Pageable pageable
    );

    @Query(
            value = "SELECT r.* FROM restaurant r " +
                    "WHERE " + BOUNDING_BOX_FILTER + " AND (" + DISTANCE_FORMULA + ") < :radius " +
                    "AND EXISTS (" +
                        "SELECT 1 FROM menu_item mi " +
                        "WHERE mi.restaurant_id = r.id AND mi.promotion_active = TRUE AND mi.available = TRUE" +
                    ") " +
                    "ORDER BY " + DISTANCE_FORMULA,
            countQuery = "SELECT COUNT(*) FROM restaurant r " +
                    "WHERE " + BOUNDING_BOX_FILTER + " AND (" + DISTANCE_FORMULA + ") < :radius " +
                    "AND EXISTS (" +
                        "SELECT 1 FROM menu_item mi " +
                        "WHERE mi.restaurant_id = r.id AND mi.promotion_active = TRUE AND mi.available = TRUE" +
                    ")",
            nativeQuery = true
    )
    Page<Restaurant> findNearbyWithPromotions(
            @Param("lat") double lat,
            @Param("lng") double lng,
            @Param("radius") double radiusInKm,
            @Param("minLat") double minLat,
            @Param("maxLat") double maxLat,
            @Param("minLng") double minLng,
            @Param("maxLng") double maxLng,
            Pageable pageable
    );

    @Query(
            value = "SELECT r.* FROM restaurant r " +
                    "WHERE " + BOUNDING_BOX_FILTER + " AND (" + DISTANCE_FORMULA + ") < :radius " +
                    "AND EXISTS (SELECT 1 FROM restaurant_category rc WHERE rc.restaurant_id = r.id AND rc.category IN (:categories)) " +
                    "ORDER BY " + DISTANCE_FORMULA,
            countQuery = "SELECT COUNT(*) FROM restaurant r " +
                    "WHERE " + BOUNDING_BOX_FILTER + " AND (" + DISTANCE_FORMULA + ") < :radius " +
                    "AND EXISTS (SELECT 1 FROM restaurant_category rc WHERE rc.restaurant_id = r.id AND rc.category IN (:categories))",
            nativeQuery = true
    )
    Page<Restaurant> findNearbyByCategory(
            @Param("lat") double lat,
            @Param("lng") double lng,
            @Param("radius") double radiusInKm,
            @Param("categories") Iterable<String> categories,
            @Param("minLat") double minLat,
            @Param("maxLat") double maxLat,
            @Param("minLng") double minLng,
            @Param("maxLng") double maxLng,
            Pageable pageable
    );

    @Query(
            value = "SELECT r.* FROM restaurant r WHERE " + BOUNDING_BOX_FILTER + " AND (" + DISTANCE_FORMULA + ") < :radius AND r.id NOT IN (:excludedIds) ORDER BY " + DISTANCE_FORMULA,
            countQuery = "SELECT COUNT(*) FROM restaurant r WHERE " + BOUNDING_BOX_FILTER + " AND (" + DISTANCE_FORMULA + ") < :radius AND r.id NOT IN (:excludedIds)",
            nativeQuery = true
    )
    Page<Restaurant> findNearbyExcluding(
            @Param("lat") double lat,
            @Param("lng") double lng,
            @Param("radius") double radiusInKm,
            @Param("excludedIds") Iterable<Long> excludedIds,
            @Param("minLat") double minLat,
            @Param("maxLat") double maxLat,
            @Param("minLng") double minLng,
            @Param("maxLng") double maxLng,
            Pageable pageable
    );

    @Query(
            value = "SELECT r.* FROM restaurant r WHERE r.id IN (:ids) AND " + BOUNDING_BOX_FILTER + " AND (" + DISTANCE_FORMULA + ") < :radius ORDER BY " + DISTANCE_FORMULA,
            countQuery = "SELECT COUNT(*) FROM restaurant r WHERE r.id IN (:ids) AND " + BOUNDING_BOX_FILTER + " AND (" + DISTANCE_FORMULA + ") < :radius",
            nativeQuery = true
    )
    Page<Restaurant> findNearbyByIds(
            @Param("lat") double lat,
            @Param("lng") double lng,
            @Param("radius") double radiusInKm,
            @Param("ids") Collection<Long> ids,
            @Param("minLat") double minLat,
            @Param("maxLat") double maxLat,
            @Param("minLng") double minLng,
            @Param("maxLng") double maxLng,
            Pageable pageable
    );

    Restaurant getRestaurantByAdmin(RestaurantAdmin admin);

    @Query("SELECT r FROM Restaurant r WHERE r.sponsored = true ORDER BY r.position ASC NULLS LAST")
    Page<Restaurant> findSponsored(Pageable pageable);
}
