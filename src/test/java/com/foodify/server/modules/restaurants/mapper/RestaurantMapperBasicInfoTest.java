package com.foodify.server.modules.restaurants.mapper;

import com.foodify.server.modules.restaurants.domain.Restaurant;
import com.foodify.server.modules.restaurants.dto.RestaurantBasicInfoDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class RestaurantMapperBasicInfoTest {

    @Autowired
    private RestaurantMapper restaurantMapper;

    @Test
    void shouldMapRestaurantToBasicInfoDto() {
        // Given a restaurant entity
        Restaurant restaurant = new Restaurant();
        restaurant.setId(1L);
        restaurant.setName("Test Restaurant");
        restaurant.setNameEn("Test Restaurant EN");
        restaurant.setNameFr("Test Restaurant FR");
        restaurant.setNameAr("Test Restaurant AR");
        restaurant.setDescription("Test Description");
        restaurant.setDescriptionEn("Test Description EN");
        restaurant.setDescriptionFr("Test Description FR");
        restaurant.setDescriptionAr("Test Description AR");
        restaurant.setRating(4.5);
        restaurant.setImageUrl("https://example.com/image.jpg");
        restaurant.setIconUrl("https://example.com/icon.jpg");
        restaurant.setAddress("123 Test St");
        restaurant.setLatitude(37.7749);
        restaurant.setLongitude(-122.4194);

        // When mapping to basic info DTO
        RestaurantBasicInfoDto dto = restaurantMapper.toBasicInfoDto(restaurant);

        // Then all basic fields should be mapped correctly
        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals("Test Restaurant", dto.getName());
        assertEquals("Test Restaurant EN", dto.getNameEn());
        assertEquals("Test Restaurant FR", dto.getNameFr());
        assertEquals("Test Restaurant AR", dto.getNameAr());
        assertEquals("Test Description", dto.getDescription());
        assertEquals("Test Description EN", dto.getDescriptionEn());
        assertEquals("Test Description FR", dto.getDescriptionFr());
        assertEquals("Test Description AR", dto.getDescriptionAr());
        assertEquals(4.5, dto.getRating());
        assertEquals("https://example.com/image.jpg", dto.getImageUrl());
        assertEquals("https://example.com/icon.jpg", dto.getIconUrl());
        assertEquals("123 Test St", dto.getAddress());
        assertEquals(37.7749, dto.getLatitude());
        assertEquals(-122.4194, dto.getLongitude());
    }

    @Test
    void shouldHandleNullValuesInRestaurantToBasicInfoDto() {
        // Given a restaurant entity with minimal data
        Restaurant restaurant = new Restaurant();
        restaurant.setId(1L);
        restaurant.setName("Test Restaurant");

        // When mapping to basic info DTO
        RestaurantBasicInfoDto dto = restaurantMapper.toBasicInfoDto(restaurant);

        // Then the mapping should succeed without errors
        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals("Test Restaurant", dto.getName());
        assertNull(dto.getDescription());
        assertNull(dto.getRating());
    }
}
