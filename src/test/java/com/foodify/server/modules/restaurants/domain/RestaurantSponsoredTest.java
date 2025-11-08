package com.foodify.server.modules.restaurants.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RestaurantSponsoredTest {

    @Test
    void restaurant_shouldHaveSponsoredFieldDefaultingToFalse() {
        // Arrange & Act
        Restaurant restaurant = new Restaurant();
        
        // Assert
        assertNotNull(restaurant.getSponsored());
        assertFalse(restaurant.getSponsored());
    }

    @Test
    void restaurant_shouldAllowSettingSponsored() {
        // Arrange
        Restaurant restaurant = new Restaurant();
        
        // Act
        restaurant.setSponsored(true);
        
        // Assert
        assertTrue(restaurant.getSponsored());
    }

    @Test
    void restaurant_shouldAllowSettingPosition() {
        // Arrange
        Restaurant restaurant = new Restaurant();
        
        // Act
        restaurant.setPosition(5);
        
        // Assert
        assertEquals(5, restaurant.getPosition());
    }

    @Test
    void restaurant_positionCanBeNull() {
        // Arrange
        Restaurant restaurant = new Restaurant();
        
        // Act & Assert
        assertNull(restaurant.getPosition());
    }

    @Test
    void restaurant_shouldAllowUpdatingPosition() {
        // Arrange
        Restaurant restaurant = new Restaurant();
        restaurant.setPosition(10);
        
        // Act
        restaurant.setPosition(20);
        
        // Assert
        assertEquals(20, restaurant.getPosition());
    }
}
