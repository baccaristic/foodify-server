package com.foodify.server.modules.restaurants.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "menu_category")
public class MenuCategory {
    @Id
    @GeneratedValue
    private Long id;

    private String name;
    
    // Multi-language support
    @Column(name = "name_en")
    private String nameEn;
    @Column(name = "name_fr")
    private String nameFr;
    @Column(name = "name_ar")
    private String nameAr;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    @JsonIgnore
    @ManyToMany(mappedBy = "categories")
    private Set<MenuItem> menuItems = new HashSet<>();
}
