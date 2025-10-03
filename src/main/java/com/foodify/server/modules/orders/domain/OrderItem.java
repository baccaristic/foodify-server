package com.foodify.server.modules.orders.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.foodify.server.modules.restaurants.domain.MenuItem;
import com.foodify.server.modules.restaurants.domain.MenuItemExtra;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class OrderItem {
    @Id @GeneratedValue
    private Long id;

    private String specialInstructions;

    @ManyToOne
    private MenuItem menuItem;


    @ManyToMany
    @JoinTable(
            name = "order_item_menu_item_extras",
            joinColumns = @JoinColumn(name = "order_item_id"),
            inverseJoinColumns = @JoinColumn(name = "menu_item_extras_id")
    )
    private List<MenuItemExtra> menuItemExtras;

    private int quantity;

    @JsonIgnore
    @ManyToOne
    private Order order;
}