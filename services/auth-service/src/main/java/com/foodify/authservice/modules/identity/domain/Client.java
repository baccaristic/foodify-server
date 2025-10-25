package com.foodify.authservice.modules.identity.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.foodify.authservice.modules.orders.domain.Order;
import com.foodify.authservice.modules.restaurants.domain.MenuItem;
import com.foodify.authservice.modules.restaurants.domain.Restaurant;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
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
    private LocalDate dateOfBirth;

    @Column(
            name = "loyalty_points_balance",
            precision = 19,
            scale = 2,
            nullable = false,
            columnDefinition = "numeric(19,2) not null default 0"
    )
    private BigDecimal loyaltyPointsBalance = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);

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