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
public class MenuOptionGroup {
    @Id
    @GeneratedValue
    private Long id;

    private String name;       // e.g. "Choose your toppings"
    
    // Multi-language support
    @Column(name = "name_en")
    private String nameEn;
    @Column(name = "name_fr")
    private String nameFr;
    @Column(name = "name_ar")
    private String nameAr;
    
    private int minSelect;     // e.g. 0, or 2 if required
    private int maxSelect;     // e.g. 3 for toppings, 6 for supplements
    private boolean required;  // e.g. true for "Choose your meat"

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "menu_item_id")
    private MenuItem menuItem;

    @OneToMany(mappedBy = "optionGroup", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MenuItemExtra> extras = new ArrayList<>();
}
