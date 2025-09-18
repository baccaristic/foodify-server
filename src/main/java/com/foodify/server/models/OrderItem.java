package com.foodify.server.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
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


    @OneToMany
    private List<MenuItemExtra> menuItemExtras;

    private int quantity;

    @JsonIgnore
    @ManyToOne
    private Order order;
}