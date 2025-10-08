package com.foodify.server.modules.restaurants.domain;

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
    @Id
    @GeneratedValue
    private Long id;

    private String name;
    private String description;
    private String category;
    private boolean isPopular;
    private double price;

    @Column(name = "promotion_label")
    private String promotionLabel;

    @Column(name = "promotion_price")
    private Double promotionPrice;

    @Column(name = "promotion_active")
    private Boolean promotionActive = Boolean.FALSE;

    // Multiple images instead of just one
    @ElementCollection
    @CollectionTable(name = "menu_item_images", joinColumns = @JoinColumn(name = "menu_item_id"))
    @Column(name = "image_url")
    private List<String> imageUrls = new ArrayList<>();

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    // Now a menu item has option groups (toppings, meat, supplements, etc.)
    @OneToMany(mappedBy = "menuItem", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MenuOptionGroup> optionGroups = new ArrayList<>();

}