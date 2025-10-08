package com.foodify.server.modules.restaurants.application;

import com.foodify.server.modules.restaurants.application.remote.dto.CatalogMenuItemExtraResponse;
import com.foodify.server.modules.restaurants.application.remote.dto.CatalogMenuItemResponse;
import com.foodify.server.modules.restaurants.application.remote.dto.CatalogRestaurantResponse;
import com.foodify.server.modules.restaurants.domain.MenuItem;
import com.foodify.server.modules.restaurants.domain.MenuItemExtra;
import com.foodify.server.modules.restaurants.domain.Restaurant;
import com.foodify.server.modules.restaurants.repository.MenuItemExtraRepository;
import com.foodify.server.modules.restaurants.repository.MenuItemRepository;
import com.foodify.server.modules.restaurants.repository.RestaurantRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class RemoteRestaurantCatalogServiceContractTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    private MockWebServer mockWebServer;
    private RestaurantRepository restaurantRepository;
    private MenuItemRepository menuItemRepository;
    private MenuItemExtraRepository menuItemExtraRepository;
    private RemoteRestaurantCatalogService service;

    @BeforeEach
    void setUp() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();

        restaurantRepository = mock(RestaurantRepository.class);
        menuItemRepository = mock(MenuItemRepository.class);
        menuItemExtraRepository = mock(MenuItemExtraRepository.class);

        RestClient.Builder builder = RestClient.builder()
                .baseUrl(mockWebServer.url("/").toString())
                .requestFactory(new SimpleClientHttpRequestFactory());

        service = new RemoteRestaurantCatalogService(builder.build(), restaurantRepository,
                menuItemRepository, menuItemExtraRepository);
    }

    @AfterEach
    void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    void getRestaurantOrThrowDelegatesToRemoteEndpoint() throws InterruptedException {
        CatalogRestaurantResponse response = new CatalogRestaurantResponse(
                42L, "Testaurant", "123 Street", "+1234567890", "ITALIAN", 4.5,
                "08:00", "23:00", "Great food", "image.jpg", true, false, false,
                2.5, "30-40", List.of(new CatalogRestaurantResponse.CatalogMenuItemSummary(101L))
        );

        mockWebServer.enqueue(jsonResponse(response));

        Restaurant restaurant = new Restaurant();
        restaurant.setId(42L);
        when(restaurantRepository.findById(42L)).thenReturn(Optional.of(restaurant));

        Restaurant resolved = service.getRestaurantOrThrow(42L);

        assertThat(resolved).isSameAs(restaurant);

        RecordedRequest request = mockWebServer.takeRequest();
        assertThat(request.getPath()).isEqualTo("/api/catalog/restaurants/42");
        assertThat(request.getMethod()).isEqualTo("GET");
    }

    @Test
    void getMenuItemOrThrowDelegatesToRemoteEndpoint() throws InterruptedException {
        CatalogMenuItemResponse response = new CatalogMenuItemResponse(
                11L, 42L, "Pizza", "Cheese pizza", "MAINS", true, 9.99,
                null, null, false, List.of("pizza.jpg"), List.of()
        );
        mockWebServer.enqueue(jsonResponse(response));

        MenuItem menuItem = new MenuItem();
        menuItem.setId(11L);
        Restaurant restaurant = new Restaurant();
        restaurant.setId(42L);
        menuItem.setRestaurant(restaurant);
        when(menuItemRepository.findById(11L)).thenReturn(Optional.of(menuItem));

        MenuItem resolved = service.getMenuItemOrThrow(11L);
        assertThat(resolved).isSameAs(menuItem);

        RecordedRequest request = mockWebServer.takeRequest();
        assertThat(request.getPath()).isEqualTo("/api/catalog/menu-items/11");
        assertThat(request.getMethod()).isEqualTo("GET");
    }

    @Test
    void getMenuItemExtrasDelegatesToRemoteEndpoint() throws InterruptedException {
        CatalogMenuItemExtraResponse extraResponse = new CatalogMenuItemExtraResponse(5L, 99L, 11L, "Cheese", 1.5, true);
        mockWebServer.enqueue(jsonResponse(List.of(extraResponse)));

        MenuItemExtra extra = new MenuItemExtra();
        extra.setId(5L);
        when(menuItemExtraRepository.findAllById(any())).thenReturn(List.of(extra));

        List<MenuItemExtra> extras = service.getMenuItemExtras(List.of(5L));
        assertThat(extras).containsExactly(extra);

        RecordedRequest request = mockWebServer.takeRequest();
        assertThat(request.getPath()).isEqualTo("/api/catalog/menu-item-extras/_lookup");
        assertThat(request.getMethod()).isEqualTo("POST");
        assertThat(request.getBody().readUtf8()).contains("\"ids\":[5]");

        verify(menuItemExtraRepository).findAllById(List.of(5L));
    }

    private MockResponse jsonResponse(Object body) {
        try {
            return new MockResponse()
                    .setHeader("Content-Type", "application/json")
                    .setBody(objectMapper.writeValueAsString(body));
        } catch (IOException e) {
            throw new IllegalStateException("Failed to serialize mock response", e);
        }
    }
}
