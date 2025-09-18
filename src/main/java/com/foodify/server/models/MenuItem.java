package com.foodify.server.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class MenuItem {
    @Id @GeneratedValue
    private Long id;
    private String name;
    private String description;
    private String category;
    private boolean isPopular;
    private double price;

    private String imageUrl;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    @OneToMany(mappedBy = "menuItem", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MenuItemExtra> extras = new ArrayList<>();
}