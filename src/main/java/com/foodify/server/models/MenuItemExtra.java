package com.foodify.server.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class MenuItemExtra {
    @Id
    @GeneratedValue
    private Long id;

    private String name;         // e.g. "Cheese"
    private double price;        // 0 if free
    private boolean required;    // if this extra must be selected

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "menu_item_id")
    private MenuItem menuItem;
}