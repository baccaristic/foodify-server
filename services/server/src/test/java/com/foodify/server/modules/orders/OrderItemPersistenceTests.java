package com.foodify.server.modules.orders;

import com.foodify.server.modules.identity.domain.AuthProvider;
import com.foodify.server.modules.identity.domain.Client;
import com.foodify.server.modules.identity.domain.RestaurantAdmin;
import com.foodify.server.modules.identity.domain.Role;
import com.foodify.server.modules.orders.domain.Order;
import com.foodify.server.modules.orders.domain.OrderItem;
import com.foodify.server.modules.orders.domain.OrderStatus;
import com.foodify.server.modules.orders.repository.OrderRepository;
import com.foodify.server.modules.restaurants.domain.MenuItem;
import com.foodify.server.modules.restaurants.domain.MenuItemExtra;
import com.foodify.server.modules.restaurants.domain.MenuOptionGroup;
import com.foodify.server.modules.restaurants.domain.Restaurant;
import com.foodify.server.modules.restaurants.repository.MenuItemRepository;
import com.foodify.server.modules.restaurants.repository.RestaurantRepository;
import com.foodify.server.modules.identity.repository.ClientRepository;
import com.foodify.server.modules.identity.repository.RestaurantAdminRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.LinkedHashSet;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

@DataJpaTest
@ActiveProfiles("test")
class OrderItemPersistenceTests {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private RestaurantAdminRepository restaurantAdminRepository;

    @Autowired
    private MenuItemRepository menuItemRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void allowsUsingTheSameExtraAcrossDifferentOrderItems() {
        RestaurantAdmin admin = new RestaurantAdmin();
        admin.setEmail("admin@example.com");
        admin.setPassword("password");
        admin.setAuthProvider(AuthProvider.LOCAL);
        admin.setRole(Role.RESTAURANT_ADMIN);
        RestaurantAdmin savedAdmin = restaurantAdminRepository.save(admin);

        Restaurant restaurant = new Restaurant();
        restaurant.setName("Testaurant");
        restaurant.setAdmin(savedAdmin);
        Restaurant savedRestaurant = restaurantRepository.save(restaurant);
        savedAdmin.setRestaurant(savedRestaurant);

        MenuItem menuItem = new MenuItem();
        menuItem.setName("Burger");
        menuItem.setPrice(10.0);
        menuItem.setRestaurant(savedRestaurant);

        MenuOptionGroup optionGroup = new MenuOptionGroup();
        optionGroup.setName("Extras");
        optionGroup.setMenuItem(menuItem);
        optionGroup.setMinSelect(0);
        optionGroup.setMaxSelect(3);
        optionGroup.setRequired(false);

        MenuItemExtra extra = new MenuItemExtra();
        extra.setName("Cheese");
        extra.setPrice(1.5);
        extra.setOptionGroup(optionGroup);

        optionGroup.getExtras().add(extra);
        menuItem.getOptionGroups().add(optionGroup);

        MenuItem savedMenuItem = menuItemRepository.save(menuItem);
        MenuItemExtra savedExtra = savedMenuItem.getOptionGroups().get(0).getExtras().get(0);

        Client client = new Client();
        client.setEmail("client@example.com");
        client.setPassword("password");
        client.setAuthProvider(AuthProvider.LOCAL);
        client.setRole(Role.CLIENT);
        Client savedClient = clientRepository.save(client);

        Order order = new Order();
        order.setClient(savedClient);
        order.setRestaurant(savedRestaurant);
        order.setStatus(OrderStatus.PENDING);
        order.setOrderTime(LocalDateTime.now());
        order.setDate(LocalDateTime.now());
        order.setPaymentMethod("CASH");
        order.setDeliveryAddress("123 Test St");

        OrderItem firstItem = new OrderItem();
        firstItem.setOrder(order);
        firstItem.setMenuItem(savedMenuItem);
        firstItem.setQuantity(1);
        firstItem.setMenuItemExtras(new LinkedHashSet<>(List.of(savedExtra)));

        OrderItem secondItem = new OrderItem();
        secondItem.setOrder(order);
        secondItem.setMenuItem(savedMenuItem);
        secondItem.setQuantity(2);
        secondItem.setMenuItemExtras(new LinkedHashSet<>(List.of(savedExtra)));

        order.setItems(new ArrayList<>(List.of(firstItem, secondItem)));

        Order persisted = orderRepository.saveAndFlush(order);

        Order reloaded = orderRepository.findById(persisted.getId()).orElseThrow();

        Assertions.assertThat(reloaded.getItems()).hasSize(2);
        Assertions.assertThat(reloaded.getItems().get(0).getMenuItemExtras())
                .extracting(MenuItemExtra::getId)
                .containsExactlyInAnyOrder(savedExtra.getId());
        Assertions.assertThat(reloaded.getItems().get(1).getMenuItemExtras())
                .extracting(MenuItemExtra::getId)
                .containsExactlyInAnyOrder(savedExtra.getId());

        Assertions.assertThat(hasUniqueConstraintOnMenuItemExtraId()).isFalse();
    }

    private boolean hasUniqueConstraintOnMenuItemExtraId() {
        try (Connection connection = Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection();
             ResultSet resultSet = connection.getMetaData().getIndexInfo(null, "PUBLIC", "ORDER_ITEM_MENU_ITEM_EXTRAS", false, false)) {
            Map<String, Set<String>> columnsByIndex = new HashMap<>();
            Map<String, Boolean> isUniqueIndex = new HashMap<>();

            while (resultSet.next()) {
                String indexName = resultSet.getString("INDEX_NAME");
                String columnName = resultSet.getString("COLUMN_NAME");
                boolean nonUnique = resultSet.getBoolean("NON_UNIQUE");

                if (indexName == null || columnName == null) {
                    continue;
                }

                columnsByIndex.computeIfAbsent(indexName, key -> new LinkedHashSet<>())
                        .add(columnName.toUpperCase());
                isUniqueIndex.put(indexName, !nonUnique);
            }

            return columnsByIndex.entrySet().stream()
                    .anyMatch(entry -> Boolean.TRUE.equals(isUniqueIndex.get(entry.getKey()))
                            && entry.getValue().size() == 1
                            && entry.getValue().contains("MENU_ITEM_EXTRA_ID"));
        } catch (SQLException exception) {
            throw new IllegalStateException("Failed to inspect database metadata", exception);
        }
    }
}
