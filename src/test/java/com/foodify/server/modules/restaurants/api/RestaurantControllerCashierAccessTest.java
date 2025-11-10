package com.foodify.server.modules.restaurants.api;

import com.foodify.server.modules.identity.domain.RestaurantAdmin;
import com.foodify.server.modules.identity.domain.RestaurantCashier;
import com.foodify.server.modules.identity.domain.Role;
import com.foodify.server.modules.identity.repository.RestaurantAdminRepository;
import com.foodify.server.modules.identity.repository.RestaurantCashierRepository;
import com.foodify.server.modules.restaurants.application.RestaurantService;
import com.foodify.server.modules.restaurants.domain.MenuCategory;
import com.foodify.server.modules.restaurants.domain.MenuItem;
import com.foodify.server.modules.restaurants.domain.Restaurant;
import com.foodify.server.modules.restaurants.dto.OperatingHoursResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Integration tests to verify that restaurant cashiers have read-only access
 * to menu, categories, and operating hours endpoints, but cannot modify them.
 */
@WebMvcTest(RestaurantController.class)
@EnableMethodSecurity(prePostEnabled = true)
class RestaurantControllerCashierAccessTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RestaurantService restaurantService;

    @MockBean
    private RestaurantAdminRepository restaurantAdminRepository;

    @MockBean
    private RestaurantCashierRepository restaurantCashierRepository;

    private Restaurant restaurant;
    private RestaurantAdmin admin;
    private RestaurantCashier cashier;

    @BeforeEach
    void setUp() {
        restaurant = new Restaurant();
        restaurant.setId(1L);
        restaurant.setName("Test Restaurant");

        admin = new RestaurantAdmin();
        admin.setId(100L);
        admin.setRole(Role.RESTAURANT_ADMIN);
        admin.setRestaurant(restaurant);

        cashier = new RestaurantCashier();
        cashier.setId(200L);
        cashier.setRole(Role.RESTAURANT_CASHIER);
        cashier.setRestaurant(restaurant);

        restaurant.setAdmin(admin);
    }

    @Test
    @WithMockUser(authorities = "ROLE_RESTAURANT_CASHIER")
    void cashierShouldBeAllowedToReadMyMenu() throws Exception {
        // Given a cashier user
        List<MenuItem> menuItems = new ArrayList<>();
        restaurant.setMenu(menuItems);
        when(restaurantCashierRepository.findById(200L)).thenReturn(Optional.of(cashier));

        // When accessing the /my-menu endpoint
        // Then should receive 200 OK (read access allowed)
        mockMvc.perform(get("/api/restaurant/my-menu")
                        .with(authentication(org.springframework.security.authentication.UsernamePasswordAuthenticationToken
                                .authenticated("200", null, List.of(new SimpleGrantedAuthority("ROLE_RESTAURANT_CASHIER"))))))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(authorities = "ROLE_RESTAURANT_CASHIER")
    void cashierShouldBeAllowedToReadCategories() throws Exception {
        // Given a cashier user
        when(restaurantCashierRepository.findById(200L)).thenReturn(Optional.of(cashier));
        when(restaurantService.getCategoriesForRestaurant(anyLong())).thenReturn(new ArrayList<>());

        // When accessing the /categories endpoint
        // Then should receive 200 OK (read access allowed)
        mockMvc.perform(get("/api/restaurant/categories")
                        .with(authentication(org.springframework.security.authentication.UsernamePasswordAuthenticationToken
                                .authenticated("200", null, List.of(new SimpleGrantedAuthority("ROLE_RESTAURANT_CASHIER"))))))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(authorities = "ROLE_RESTAURANT_CASHIER")
    void cashierShouldBeAllowedToReadOperatingHours() throws Exception {
        // Given a cashier user
        when(restaurantCashierRepository.findById(200L)).thenReturn(Optional.of(cashier));
        OperatingHoursResponse response = new OperatingHoursResponse(new ArrayList<>(), new ArrayList<>());
        when(restaurantService.getOperatingHours(anyLong())).thenReturn(response);

        // When accessing the /operating-hours endpoint
        // Then should receive 200 OK (read access allowed)
        mockMvc.perform(get("/api/restaurant/operating-hours")
                        .with(authentication(org.springframework.security.authentication.UsernamePasswordAuthenticationToken
                                .authenticated("200", null, List.of(new SimpleGrantedAuthority("ROLE_RESTAURANT_CASHIER"))))))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(authorities = "ROLE_RESTAURANT_ADMIN")
    void adminShouldBeAllowedAccessToMyMenu() throws Exception {
        // Given an admin user
        List<MenuItem> menuItems = new ArrayList<>();
        restaurant.setMenu(menuItems);
        when(restaurantAdminRepository.findById(100L)).thenReturn(Optional.of(admin));

        // When accessing the /my-menu endpoint
        // Then should receive 200 OK
        mockMvc.perform(get("/api/restaurant/my-menu")
                        .with(authentication(org.springframework.security.authentication.UsernamePasswordAuthenticationToken
                                .authenticated("100", null, List.of(new SimpleGrantedAuthority("ROLE_RESTAURANT_ADMIN"))))))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(authorities = "ROLE_RESTAURANT_ADMIN")
    void adminShouldBeAllowedAccessToCategories() throws Exception {
        // Given an admin user
        when(restaurantAdminRepository.findById(100L)).thenReturn(Optional.of(admin));
        when(restaurantService.getCategoriesForRestaurant(anyLong())).thenReturn(new ArrayList<>());

        // When accessing the /categories endpoint
        // Then should receive 200 OK
        mockMvc.perform(get("/api/restaurant/categories")
                        .with(authentication(org.springframework.security.authentication.UsernamePasswordAuthenticationToken
                                .authenticated("100", null, List.of(new SimpleGrantedAuthority("ROLE_RESTAURANT_ADMIN"))))))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(authorities = "ROLE_RESTAURANT_ADMIN")
    void adminShouldBeAllowedAccessToOperatingHours() throws Exception {
        // Given an admin user
        when(restaurantAdminRepository.findById(100L)).thenReturn(Optional.of(admin));
        OperatingHoursResponse response = new OperatingHoursResponse(new ArrayList<>(), new ArrayList<>());
        when(restaurantService.getOperatingHours(anyLong())).thenReturn(response);

        // When accessing the /operating-hours endpoint
        // Then should receive 200 OK
        mockMvc.perform(get("/api/restaurant/operating-hours")
                        .with(authentication(org.springframework.security.authentication.UsernamePasswordAuthenticationToken
                                .authenticated("100", null, List.of(new SimpleGrantedAuthority("ROLE_RESTAURANT_ADMIN"))))))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(authorities = "ROLE_RESTAURANT_CASHIER")
    void cashierShouldBeDeniedFromCreatingCategories() throws Exception {
        // Given a cashier user trying to create a category
        // When accessing the POST /categories endpoint
        // Then should receive 403 Forbidden (write access denied)
        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post("/api/restaurant/categories")
                        .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"New Category\"}")
                        .with(authentication(org.springframework.security.authentication.UsernamePasswordAuthenticationToken
                                .authenticated("200", null, List.of(new SimpleGrantedAuthority("ROLE_RESTAURANT_CASHIER"))))))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(authorities = "ROLE_RESTAURANT_CASHIER")
    void cashierShouldBeDeniedFromUpdatingOperatingHours() throws Exception {
        // Given a cashier user trying to update operating hours
        // When accessing the PUT /operating-hours/weekly endpoint
        // Then should receive 403 Forbidden (write access denied)
        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put("/api/restaurant/operating-hours/weekly")
                        .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                        .content("{\"weeklySchedule\":[]}")
                        .with(authentication(org.springframework.security.authentication.UsernamePasswordAuthenticationToken
                                .authenticated("200", null, List.of(new SimpleGrantedAuthority("ROLE_RESTAURANT_CASHIER"))))))
                .andExpect(status().isForbidden());
    }
}
