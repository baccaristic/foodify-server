package com.foodify.server.modules.orders.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.foodify.server.modules.restaurants.domain.MenuItem;
import com.foodify.server.modules.restaurants.domain.MenuItemExtra;
import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
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
    @ManyToOne(optional = false)
    @JoinColumn(name = "order_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Order order;
}
