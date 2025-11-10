package com.foodify.server.modules.restaurants.domain;

import com.foodify.server.modules.identity.domain.RestaurantCashier;
import com.foodify.server.modules.identity.domain.Role;
import org.junit.jupiter.api.Test;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

class RestaurantCashierRelationTest {

    @Test
    void restaurant_shouldHaveCashiersCollection() {
        // Arrange & Act
        Restaurant restaurant = new Restaurant();
        
        // Assert
        assertNotNull(restaurant.getCashiers());
        assertTrue(restaurant.getCashiers() instanceof HashSet);
        assertTrue(restaurant.getCashiers().isEmpty());
    }

    @Test
    void restaurant_shouldAllowAddingCashier() {
        // Arrange
        Restaurant restaurant = new Restaurant();
        restaurant.setName("Test Restaurant");
        
        RestaurantCashier cashier = new RestaurantCashier();
        cashier.setName("John Doe");
        cashier.setEmail("john@example.com");
        cashier.setRole(Role.RESTAURANT_CASHIER);
        cashier.setRestaurant(restaurant);
        
        // Act
        restaurant.getCashiers().add(cashier);
        
        // Assert
        assertEquals(1, restaurant.getCashiers().size());
        assertTrue(restaurant.getCashiers().contains(cashier));
    }

    @Test
    void restaurant_shouldAllowMultipleCashiers() {
        // Arrange
        Restaurant restaurant = new Restaurant();
        restaurant.setName("Test Restaurant");
        
        RestaurantCashier cashier1 = new RestaurantCashier();
        cashier1.setName("John Doe");
        cashier1.setEmail("john@example.com");
        cashier1.setRole(Role.RESTAURANT_CASHIER);
        cashier1.setRestaurant(restaurant);
        
        RestaurantCashier cashier2 = new RestaurantCashier();
        cashier2.setName("Jane Smith");
        cashier2.setEmail("jane@example.com");
        cashier2.setRole(Role.RESTAURANT_CASHIER);
        cashier2.setRestaurant(restaurant);
        
        // Act
        restaurant.getCashiers().add(cashier1);
        restaurant.getCashiers().add(cashier2);
        
        // Assert
        assertEquals(2, restaurant.getCashiers().size());
        assertTrue(restaurant.getCashiers().contains(cashier1));
        assertTrue(restaurant.getCashiers().contains(cashier2));
    }

    @Test
    void restaurantCashier_shouldHaveRestaurantReference() {
        // Arrange
        Restaurant restaurant = new Restaurant();
        restaurant.setName("Test Restaurant");
        
        RestaurantCashier cashier = new RestaurantCashier();
        cashier.setName("John Doe");
        cashier.setEmail("john@example.com");
        cashier.setRole(Role.RESTAURANT_CASHIER);
        
        // Act
        cashier.setRestaurant(restaurant);
        
        // Assert
        assertNotNull(cashier.getRestaurant());
        assertEquals(restaurant, cashier.getRestaurant());
        assertEquals("Test Restaurant", cashier.getRestaurant().getName());
    }

    @Test
    void restaurantCashier_shouldAllowNullRestaurant() {
        // Arrange
        RestaurantCashier cashier = new RestaurantCashier();
        cashier.setName("John Doe");
        cashier.setEmail("john@example.com");
        
        // Act & Assert
        assertNull(cashier.getRestaurant());
    }

    @Test
    void restaurant_shouldAllowRemovingCashier() {
        // Arrange
        Restaurant restaurant = new Restaurant();
        restaurant.setName("Test Restaurant");
        
        RestaurantCashier cashier = new RestaurantCashier();
        cashier.setName("John Doe");
        cashier.setEmail("john@example.com");
        cashier.setRole(Role.RESTAURANT_CASHIER);
        cashier.setRestaurant(restaurant);
        
        restaurant.getCashiers().add(cashier);
        
        // Act
        restaurant.getCashiers().remove(cashier);
        
        // Assert
        assertTrue(restaurant.getCashiers().isEmpty());
    }
}
