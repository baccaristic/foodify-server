package com.foodify.server.models;
import com.fasterxml.jackson.annotation.JsonIgnore;
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