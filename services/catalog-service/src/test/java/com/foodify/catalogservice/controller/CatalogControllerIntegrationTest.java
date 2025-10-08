package com.foodify.catalogservice.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.foodify.catalogservice.domain.MenuItem;
import com.foodify.catalogservice.domain.MenuItemExtra;
import com.foodify.catalogservice.domain.Restaurant;
import com.foodify.catalogservice.repository.MenuItemExtraRepository;
import com.foodify.catalogservice.repository.MenuItemRepository;
import com.foodify.catalogservice.repository.RestaurantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CatalogControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private MenuItemRepository menuItemRepository;

    @Autowired
    private MenuItemExtraRepository menuItemExtraRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Restaurant restaurant;
    private MenuItem menuItem;

    @BeforeEach
    void setUp() {
        menuItemExtraRepository.deleteAll();
        menuItemRepository.deleteAll();
        restaurantRepository.deleteAll();

        restaurant = new Restaurant();
        restaurant.setName("Integration Eats");
        restaurant.setDescription("Great food");
        restaurant.setTopChoice(true);
        restaurant.setFreeDelivery(true);
        restaurant.setTopEat(true);
        restaurant.setDeliveryFee(3.0);
        restaurant.setDeliveryTimeRange("20-30");
        restaurant.setRating(4.9);
        restaurant.setOpeningHours("08:00");
        restaurant.setClosingHours("22:00");
        restaurant = restaurantRepository.save(restaurant);

        menuItem = new MenuItem();
        menuItem.setName("Signature");
        menuItem.setDescription("Popular choice");
        menuItem.setCategory("MAINS");
        menuItem.setPopular(true);
        menuItem.setPrice(15.0);
        menuItem.setPromotionActive(true);
        menuItem.setPromotionPrice(12.0);
        menuItem.setPromotionLabel("SPECIAL");
        menuItem.setRestaurant(restaurant);
        menuItem = menuItemRepository.save(menuItem);

        MenuItemExtra extra = new MenuItemExtra();
        extra.setName("Cheese");
        extra.setPrice(2.5);
        extra.setDefault(true);
        extra.setOptionGroup(null);

        menuItemExtraRepository.save(extra);
    }

    @Test
    void searchRestaurantsReturnsPromotions() throws Exception {
        String response = mockMvc.perform(get("/api/catalog/restaurants/_search")
                        .param("query", "Integration")
                        .param("hasPromotion", "true")
                        .param("page", "1")
                        .param("pageSize", "10")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        JsonNode json = objectMapper.readTree(response);
        assertThat(json.get("items")).isNotNull();
        assertThat(json.get("items")).hasSize(1);
        JsonNode item = json.get("items").get(0);
        assertThat(item.get("id").asLong()).isEqualTo(restaurant.getId());
        assertThat(item.get("promotedMenuItems")).hasSize(1);
        assertThat(item.get("promotedMenuItems").get(0).get("promotionPrice").asDouble()).isEqualTo(12.0);
    }

    @Test
    void availabilityEndpointHonoursQueryInstant() throws Exception {
        Instant at = Instant.parse("2024-05-01T09:30:00Z");
        String response = mockMvc.perform(get("/api/catalog/restaurants/" + restaurant.getId() + "/availability")
                        .param("at", at.toString())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        JsonNode json = objectMapper.readTree(response);
        assertThat(json.get("available").asBoolean()).isTrue();
        assertThat(json.get("openingHours").asText()).isEqualTo("08:00");
    }

    @Test
    void pricingEndpointCachesLatestValues() throws Exception {
        String firstResponse = mockMvc.perform(get("/api/catalog/menu-items/" + menuItem.getId() + "/pricing")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        JsonNode json = objectMapper.readTree(firstResponse);
        assertThat(json.get("promotionActive").asBoolean()).isTrue();

        menuItem.setPromotionActive(false);
        menuItemRepository.save(menuItem);

        String cachedResponse = mockMvc.perform(get("/api/catalog/menu-items/" + menuItem.getId() + "/pricing")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        JsonNode cachedJson = objectMapper.readTree(cachedResponse);
        assertThat(cachedJson.get("promotionActive").asBoolean()).isFalse();
        assertThat(cachedJson.get("lastSynced").asText()).isNotBlank();
    }
}
