package com.foodify.server.modules.identity.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RoleTest {

    @Test
    void shouldContainRestaurantCashierRole() {
        // Verify that RESTAURANT_CASHIER role exists in the Role enum
        Role[] roles = Role.values();
        
        boolean containsRestaurantCashier = false;
        for (Role role : roles) {
            if (role == Role.RESTAURANT_CASHIER) {
                containsRestaurantCashier = true;
                break;
            }
        }
        
        assertTrue(containsRestaurantCashier, "RESTAURANT_CASHIER role should exist in Role enum");
    }

    @Test
    void shouldHaveFiveRoles() {
        // Verify that we have exactly 5 roles: CLIENT, DRIVER, RESTAURANT_ADMIN, RESTAURANT_CASHIER, ADMIN
        Role[] roles = Role.values();
        assertEquals(5, roles.length, "Should have exactly 5 roles");
    }

    @Test
    void shouldBeAbleToGetRestaurantCashierByName() {
        // Verify that we can get RESTAURANT_CASHIER role by name
        Role role = Role.valueOf("RESTAURANT_CASHIER");
        assertNotNull(role, "Should be able to get RESTAURANT_CASHIER by name");
        assertEquals(Role.RESTAURANT_CASHIER, role);
    }
}
