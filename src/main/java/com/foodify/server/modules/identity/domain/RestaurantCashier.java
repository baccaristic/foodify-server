package com.foodify.server.modules.identity.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.foodify.server.modules.restaurants.domain.Restaurant;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class RestaurantCashier extends User {
    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    @JsonIgnore
    private Restaurant restaurant;
}
