package com.foodify.server.modules.restaurants.domain;

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

    private String name;       // e.g. "Onion +", "Cordon bleu", "Cheddar ++"
    private double price;      // 0 if included, >0 if extra cost
    private boolean isDefault; // optional: pre-selected in frontend

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "option_group_id")
    private MenuOptionGroup optionGroup;
}