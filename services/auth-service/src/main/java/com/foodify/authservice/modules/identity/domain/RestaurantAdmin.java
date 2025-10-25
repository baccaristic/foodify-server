package com.foodify.authservice.modules.identity.domain;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.foodify.authservice.modules.restaurants.domain.Restaurant;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class RestaurantAdmin extends User {
    @OneToOne(mappedBy = "admin")
    @JsonIgnore
    private Restaurant restaurant;
}