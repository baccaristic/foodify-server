package com.foodify.server.modules.identity.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.foodify.server.modules.orders.domain.Order;
import com.foodify.server.modules.restaurants.domain.MenuItem;
import com.foodify.server.modules.restaurants.domain.Restaurant;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
public class Client extends User {
    private String address;
    private String phoneNumber;
    private Boolean phoneVerified;
    private Boolean emailVerified;
    private String googleId;

    @JsonIgnore
    @OneToMany(mappedBy = "client")
    private List<Order> orders;

    @JsonIgnore
    @ManyToMany
    @JoinTable(
            name = "client_favorite_restaurants",
            joinColumns = @JoinColumn(name = "client_id"),
            inverseJoinColumns = @JoinColumn(name = "restaurant_id")
    )
    private Set<Restaurant> favoriteRestaurants = new HashSet<>();

    @JsonIgnore
    @ManyToMany
    @JoinTable(
            name = "client_favorite_menu_items",
            joinColumns = @JoinColumn(name = "client_id"),
            inverseJoinColumns = @JoinColumn(name = "menu_item_id")
    )
    private Set<MenuItem> favoriteMenuItems = new HashSet<>();
}