package com.foodify.server.modules.orders.application;

import com.foodify.server.modules.addresses.application.SavedAddressDirectoryService;
import com.foodify.server.modules.identity.application.ClientDirectoryService;
import com.foodify.server.modules.identity.domain.AuthProvider;
import com.foodify.server.modules.identity.domain.Client;
import com.foodify.server.modules.identity.domain.RestaurantAdmin;
import com.foodify.server.modules.identity.domain.Role;
import com.foodify.server.modules.orders.domain.Order;
import com.foodify.server.modules.orders.dto.LocationDto;
import com.foodify.server.modules.orders.dto.OrderItemRequest;
import com.foodify.server.modules.orders.dto.OrderRequest;
import com.foodify.server.modules.orders.dto.response.CreateOrderResponse;
import com.foodify.server.modules.orders.mapper.OrderNotificationMapper;
import com.foodify.server.modules.orders.repository.OrderRepository;
import com.foodify.server.modules.restaurants.application.RestaurantCatalogService;
import com.foodify.server.modules.restaurants.domain.MenuItem;
import com.foodify.server.modules.restaurants.domain.MenuItemExtra;
import com.foodify.server.modules.restaurants.domain.MenuOptionGroup;
import com.foodify.server.modules.restaurants.domain.Restaurant;
import com.foodify.server.modules.restaurants.sync.CatalogMenuItemExtraSnapshot;
import com.foodify.server.modules.restaurants.sync.CatalogMenuItemSnapshot;
import com.foodify.server.modules.restaurants.sync.CatalogRestaurantSnapshot;
import com.foodify.server.modules.restaurants.sync.CatalogSnapshotCache;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomerOrderServicePricingConsistencyTest {

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private ClientDirectoryService clientDirectoryService;
    @Mock
    private RestaurantCatalogService restaurantCatalogService;
    @Mock
    private OrderLifecycleService orderLifecycleService;
    @Mock
    private SavedAddressDirectoryService savedAddressDirectoryService;
    @Mock
    private OrderNotificationMapper orderNotificationMapper;

    private CatalogSnapshotCache catalogSnapshotCache;

    private CustomerOrderService customerOrderService;

    @BeforeEach
    void setUp() {
        catalogSnapshotCache = new CatalogSnapshotCache();
        customerOrderService = new CustomerOrderService(
                orderRepository,
                clientDirectoryService,
                restaurantCatalogService,
                orderLifecycleService,
                savedAddressDirectoryService,
                orderNotificationMapper,
                catalogSnapshotCache
        );
    }

    @Test
    void appliesCatalogPricingSnapshotsWhenPromotionsOrExtrasChange() {
        Long clientId = 15L;
        Long restaurantId = 30L;
        Long menuItemId = 50L;
        Long extraId = 70L;

        Client client = new Client();
        client.setId(clientId);
        client.setEmail("client@example.com");
        client.setPassword("secret");
        client.setAuthProvider(AuthProvider.LOCAL);
        client.setRole(Role.CLIENT);
        when(clientDirectoryService.getClientOrThrow(clientId)).thenReturn(client);

        when(orderRepository.existsByClient_IdAndStatusInAndArchivedAtIsNull(eq(clientId), anyList())).thenReturn(false);

        RestaurantAdmin admin = new RestaurantAdmin();
        admin.setId(200L);
        admin.setEmail("admin@example.com");
        admin.setPassword("pw");
        admin.setAuthProvider(AuthProvider.LOCAL);
        admin.setRole(Role.RESTAURANT_ADMIN);

        Restaurant restaurant = new Restaurant();
        restaurant.setId(restaurantId);
        restaurant.setName("Tasty Meals");
        restaurant.setLatitude(12.3);
        restaurant.setLongitude(-45.6);
        restaurant.setAdmin(admin);
        when(restaurantCatalogService.getRestaurantOrThrow(restaurantId)).thenReturn(restaurant);

        MenuItem menuItem = new MenuItem();
        menuItem.setId(menuItemId);
        menuItem.setName("Pasta");
        menuItem.setPrice(9.0);
        menuItem.setPromotionActive(false);
        menuItem.setRestaurant(restaurant);
        when(restaurantCatalogService.getMenuItemOrThrow(menuItemId)).thenReturn(menuItem);

        MenuOptionGroup optionGroup = new MenuOptionGroup();
        optionGroup.setMenuItem(menuItem);

        MenuItemExtra extra = new MenuItemExtra();
        extra.setId(extraId);
        extra.setName("Cheese");
        extra.setPrice(1.0);
        extra.setOptionGroup(optionGroup);
        when(restaurantCatalogService.getMenuItemExtras(List.of(extraId))).thenReturn(List.of(extra));

        catalogSnapshotCache.putMenuItem(new CatalogMenuItemSnapshot(menuItemId, restaurantId, 9.0, 7.0, true, true));
        catalogSnapshotCache.putExtra(new CatalogMenuItemExtraSnapshot(extraId, menuItemId, "Cheese", 3.0, true));

        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> {
            Order order = invocation.getArgument(0);
            order.setId(999L);
            order.setDate(LocalDateTime.now());
            return order;
        });
        doNothing().when(orderLifecycleService).registerCreation(any(Order.class), eq("client:" + clientId));

        OrderRequest request = new OrderRequest();
        request.setDeliveryAddress("123 Main");
        request.setLocation(new LocationDto(10.0, 20.0));
        request.setPaymentMethod("CARD");
        request.setRestaurantId(restaurantId);
        request.setItems(List.of(new OrderItemRequest() {{
            setMenuItemId(menuItemId);
            setQuantity(1);
            setExtraIds(List.of(extraId));
        }}));

        CreateOrderResponse response = customerOrderService.placeOrder(clientId, request);

        assertThat(response.payment().subtotal()).isEqualByComparingTo(BigDecimal.valueOf(7.0));
        assertThat(response.payment().extrasTotal()).isEqualByComparingTo(BigDecimal.valueOf(3.0));
        assertThat(response.payment().total()).isEqualByComparingTo(BigDecimal.valueOf(10.0));
        assertThat(response.items()).hasSize(1);
        assertThat(response.items().get(0).unitPrice()).isEqualByComparingTo(BigDecimal.valueOf(7.0));
        assertThat(response.items().get(0).extras()).hasSize(1);
        assertThat(response.items().get(0).extras().get(0).price()).isEqualByComparingTo(BigDecimal.valueOf(3.0));
        assertThat(response.items().get(0).extras().get(0).id()).isEqualTo(extraId);

        ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);
        verify(orderRepository).save(orderCaptor.capture());
        Order persisted = orderCaptor.getValue();
        assertThat(persisted.getItems()).hasSize(1);
        assertThat(persisted.getItems().get(0).getCatalogItem().getPromotionActive()).isTrue();
        assertThat(persisted.getItems().get(0).getCatalogItem().getPromotionPrice()).isEqualTo(7.0);
        assertThat(persisted.getItems().get(0).getMenuItemExtras().get(0).getPrice()).isEqualTo(3.0);
    }

    @Test
    void throwsWhenCatalogReportsMenuItemUnavailable() {
        Long clientId = 5L;
        Long restaurantId = 7L;
        Long menuItemId = 9L;

        Client client = new Client();
        client.setId(clientId);
        when(clientDirectoryService.getClientOrThrow(clientId)).thenReturn(client);
        when(orderRepository.existsByClient_IdAndStatusInAndArchivedAtIsNull(eq(clientId), anyList())).thenReturn(false);

        Restaurant restaurant = new Restaurant();
        restaurant.setId(restaurantId);
        when(restaurantCatalogService.getRestaurantOrThrow(restaurantId)).thenReturn(restaurant);

        MenuItem menuItem = new MenuItem();
        menuItem.setId(menuItemId);
        menuItem.setRestaurant(restaurant);
        when(restaurantCatalogService.getMenuItemOrThrow(menuItemId)).thenReturn(menuItem);

        catalogSnapshotCache.putMenuItem(new CatalogMenuItemSnapshot(menuItemId, restaurantId, 10.0, null, null, false));

        OrderRequest request = new OrderRequest();
        request.setRestaurantId(restaurantId);
        request.setPaymentMethod("CARD");
        request.setDeliveryAddress("123 St");
        request.setItems(List.of(new OrderItemRequest() {{
            setMenuItemId(menuItemId);
            setQuantity(1);
        }}));

        assertThatThrownBy(() -> customerOrderService.placeOrder(clientId, request))
                .isInstanceOf(ResponseStatusException.class)
                .satisfies(ex -> assertThat(((ResponseStatusException) ex).getStatusCode().value())
                        .isEqualTo(HttpStatus.BAD_REQUEST.value()));
    }

    @Test
    void throwsWhenCatalogReportsExtraUnavailable() {
        Long clientId = 11L;
        Long restaurantId = 22L;
        Long menuItemId = 33L;
        Long extraId = 44L;

        Client client = new Client();
        client.setId(clientId);
        when(clientDirectoryService.getClientOrThrow(clientId)).thenReturn(client);
        when(orderRepository.existsByClient_IdAndStatusInAndArchivedAtIsNull(eq(clientId), anyList())).thenReturn(false);

        Restaurant restaurant = new Restaurant();
        restaurant.setId(restaurantId);
        when(restaurantCatalogService.getRestaurantOrThrow(restaurantId)).thenReturn(restaurant);

        MenuItem menuItem = new MenuItem();
        menuItem.setId(menuItemId);
        menuItem.setRestaurant(restaurant);
        when(restaurantCatalogService.getMenuItemOrThrow(menuItemId)).thenReturn(menuItem);

        MenuOptionGroup optionGroup = new MenuOptionGroup();
        optionGroup.setMenuItem(menuItem);

        MenuItemExtra extra = new MenuItemExtra();
        extra.setId(extraId);
        extra.setOptionGroup(optionGroup);
        when(restaurantCatalogService.getMenuItemExtras(List.of(extraId))).thenReturn(List.of(extra));

        catalogSnapshotCache.putMenuItem(new CatalogMenuItemSnapshot(menuItemId, restaurantId, 10.0, null, null, true));
        catalogSnapshotCache.putExtra(new CatalogMenuItemExtraSnapshot(extraId, menuItemId, "Sauce", 1.5, false));

        OrderRequest request = new OrderRequest();
        request.setRestaurantId(restaurantId);
        request.setPaymentMethod("CARD");
        request.setDeliveryAddress("123 St");
        request.setItems(List.of(new OrderItemRequest() {{
            setMenuItemId(menuItemId);
            setQuantity(1);
            setExtraIds(List.of(extraId));
        }}));

        assertThatThrownBy(() -> customerOrderService.placeOrder(clientId, request))
                .isInstanceOf(ResponseStatusException.class)
                .satisfies(ex -> assertThat(((ResponseStatusException) ex).getStatusCode().value())
                        .isEqualTo(HttpStatus.BAD_REQUEST.value()));
    }

    @Test
    void overlaysRestaurantSnapshotFromCatalogCache() {
        Long clientId = 60L;
        Long restaurantId = 61L;
        Long menuItemId = 62L;

        Client client = new Client();
        client.setId(clientId);
        when(clientDirectoryService.getClientOrThrow(clientId)).thenReturn(client);
        when(orderRepository.existsByClient_IdAndStatusInAndArchivedAtIsNull(eq(clientId), anyList())).thenReturn(false);

        Restaurant restaurant = new Restaurant();
        restaurant.setId(restaurantId);
        restaurant.setName("Local Name");
        restaurant.setAddress("Local Address");
        restaurant.setPhone("111-222");
        restaurant.setImageUrl("local.png");
        restaurant.setLatitude(1.0);
        restaurant.setLongitude(2.0);
        when(restaurantCatalogService.getRestaurantOrThrow(restaurantId)).thenReturn(restaurant);

        MenuItem menuItem = new MenuItem();
        menuItem.setId(menuItemId);
        menuItem.setRestaurant(restaurant);
        menuItem.setName("Burger");
        menuItem.setPrice(12.0);
        when(restaurantCatalogService.getMenuItemOrThrow(menuItemId)).thenReturn(menuItem);

        catalogSnapshotCache.putRestaurant(new CatalogRestaurantSnapshot(
                restaurantId,
                999L,
                "Remote Name",
                "Remote Address",
                "555-999",
                "remote.png",
                3.3,
                4.4
        ));
        catalogSnapshotCache.putMenuItem(new CatalogMenuItemSnapshot(menuItemId, restaurantId, 14.0, null, null, true));

        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> {
            Order order = invocation.getArgument(0);
            order.setId(321L);
            order.setDate(LocalDateTime.now());
            return order;
        });
        doNothing().when(orderLifecycleService).registerCreation(any(Order.class), any());

        OrderRequest request = new OrderRequest();
        request.setRestaurantId(restaurantId);
        request.setPaymentMethod("CARD");
        request.setDeliveryAddress("123 St");
        request.setItems(List.of(new OrderItemRequest() {{
            setMenuItemId(menuItemId);
            setQuantity(1);
        }}));

        CreateOrderResponse response = customerOrderService.placeOrder(clientId, request);

        assertThat(response.restaurant().name()).isEqualTo("Remote Name");

        ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);
        verify(orderRepository).save(orderCaptor.capture());
        Order persisted = orderCaptor.getValue();
        assertThat(persisted.getRestaurant().getName()).isEqualTo("Remote Name");
        assertThat(persisted.getRestaurant().getAddress()).isEqualTo("Remote Address");
        assertThat(persisted.getRestaurant().getPhone()).isEqualTo("555-999");
        assertThat(persisted.getRestaurant().getImageUrl()).isEqualTo("remote.png");
        assertThat(persisted.getRestaurant().getLatitude()).isEqualTo(3.3);
        assertThat(persisted.getRestaurant().getLongitude()).isEqualTo(4.4);
        assertThat(persisted.getRestaurant().getAdminId()).isEqualTo(999L);
    }
}
