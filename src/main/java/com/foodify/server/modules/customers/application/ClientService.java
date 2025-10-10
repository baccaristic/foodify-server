package com.foodify.server.modules.customers.application;

import com.foodify.server.modules.customers.dto.ClientFavoriteIds;
import com.foodify.server.modules.customers.dto.ClientFavoritesResponse;
import com.foodify.server.modules.customers.dto.MenuItemFavoriteDto;
import com.foodify.server.modules.identity.domain.Client;
import com.foodify.server.modules.identity.repository.ClientRepository;
import com.foodify.server.modules.orders.domain.Order;
import com.foodify.server.modules.orders.dto.OrderDto;
import com.foodify.server.modules.orders.mapper.OrderMapper;
import com.foodify.server.modules.orders.repository.OrderRepository;
import com.foodify.server.modules.restaurants.domain.MenuItem;
import com.foodify.server.modules.restaurants.domain.Restaurant;
import com.foodify.server.modules.restaurants.dto.RestaurantDisplayDto;
import com.foodify.server.modules.restaurants.mapper.RestaurantMapper;
import com.foodify.server.modules.restaurants.repository.MenuItemRepository;
import com.foodify.server.modules.restaurants.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;
    private final OrderRepository  orderRepository;
    private final RestaurantRepository restaurantRepository;
    private final MenuItemRepository menuItemRepository;
    private final RestaurantMapper restaurantMapper;


    @Transactional(readOnly = true)
    public List<OrderDto> getMyOrders(Client client) {
        if (client == null) {
            return List.of();
        }

        return this.orderRepository.findAllByClientOrderByDateDesc(client)
                .stream()
                .map(OrderMapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public Order getMyOrder(Long id) {
        return this.orderRepository.findById(id).orElse(null);
    }

    @Transactional
    public void addFavoriteRestaurant(Long clientId, Long restaurantId) {
        Client client = getClientOrThrow(clientId);
        Restaurant restaurant = getRestaurantOrThrow(restaurantId);
        client.getFavoriteRestaurants().add(restaurant);
    }

    @Transactional
    public void removeFavoriteRestaurant(Long clientId, Long restaurantId) {
        Client client = getClientOrThrow(clientId);
        client.getFavoriteRestaurants().removeIf(restaurant -> Optional.ofNullable(restaurant.getId()).map(id -> id.equals(restaurantId)).orElse(false));
    }

    @Transactional
    public void addFavoriteMenuItem(Long clientId, Long menuItemId) {
        Client client = getClientOrThrow(clientId);
        MenuItem menuItem = getMenuItemOrThrow(menuItemId);
        client.getFavoriteMenuItems().add(menuItem);
    }

    @Transactional
    public void removeFavoriteMenuItem(Long clientId, Long menuItemId) {
        Client client = getClientOrThrow(clientId);
        client.getFavoriteMenuItems().removeIf(item -> Optional.ofNullable(item.getId()).map(id -> id.equals(menuItemId)).orElse(false));
    }

    @Transactional(readOnly = true)
    public ClientFavoritesResponse getFavorites(Long clientId) {
        Client client = getClientOrThrow(clientId);

        List<Restaurant> restaurants = client.getFavoriteRestaurants().stream()
                .sorted(Comparator.comparing(Restaurant::getName, Comparator.nullsLast(String::compareToIgnoreCase)))
                .toList();

        List<MenuItem> menuItems = client.getFavoriteMenuItems().stream()
                .sorted(Comparator.comparing(MenuItem::getName, Comparator.nullsLast(String::compareToIgnoreCase)))
                .toList();

        List<RestaurantDisplayDto> restaurantDtos = restaurants.stream()
                .map(restaurantMapper::toDto)
                .collect(Collectors.toCollection(ArrayList::new));
        restaurantDtos.forEach(dto -> dto.setFavorite(true));

        List<MenuItemFavoriteDto> menuItemDtos = menuItems.stream()
                .map(this::toFavoriteDto)
                .toList();

        return new ClientFavoritesResponse(
                restaurantDtos,
                menuItemDtos
        );
    }

    @Transactional(readOnly = true)
    public ClientFavoriteIds getFavoriteIds(Long clientId) {
        Client client = getClientOrThrow(clientId);

        Set<Long> restaurantIds = client.getFavoriteRestaurants().stream()
                .map(Restaurant::getId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        Set<Long> menuItemIds = client.getFavoriteMenuItems().stream()
                .map(MenuItem::getId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        return new ClientFavoriteIds(restaurantIds, menuItemIds);
    }

    private Client getClientOrThrow(Long clientId) {
        return clientRepository.findById(clientId)
                .orElseThrow(() -> new EntityNotFoundException("Client not found: " + clientId));
    }

    private Restaurant getRestaurantOrThrow(Long restaurantId) {
        return restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new EntityNotFoundException("Restaurant not found: " + restaurantId));
    }

    private MenuItem getMenuItemOrThrow(Long menuItemId) {
        return menuItemRepository.findById(menuItemId)
                .orElseThrow(() -> new EntityNotFoundException("Menu item not found: " + menuItemId));
    }

    private MenuItemFavoriteDto toFavoriteDto(MenuItem item) {
        String imageUrl = Optional.ofNullable(item.getImageUrls())
                .filter(list -> !list.isEmpty())
                .map(list -> list.get(0))
                .orElse(null);

        Restaurant restaurant = item.getRestaurant();
        Long restaurantId = restaurant != null ? restaurant.getId() : null;
        String restaurantName = restaurant != null ? restaurant.getName() : null;

        return new MenuItemFavoriteDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getPrice(),
                imageUrl,
                item.isPopular(),
                item.getPromotionPrice(),
                item.getPromotionLabel(),
                Boolean.TRUE.equals(item.getPromotionActive()),
                true,
                restaurantId,
                restaurantName
        );
    }
}
