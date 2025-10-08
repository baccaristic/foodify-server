package com.foodify.catalogservice.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "restaurant")
@Getter
@Setter
public class Restaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String address;
    private String phone;
    private String type;
    private Double rating;
    @Column(name = "opening_hours")
    private String openingHours;
    @Column(name = "closing_hours")
    private String closingHours;
    private String description;
    private String imageUrl;
    @Column(name = "top_choice")
    private Boolean topChoice;
    @Column(name = "free_delivery")
    private Boolean freeDelivery;
    @Column(name = "top_eat")
    private Boolean topEat;
    @Column(name = "delivery_fee")
    private Double deliveryFee;
    @Column(name = "delivery_time_range")
    private String deliveryTimeRange;

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL)
    private List<MenuItem> menuItems = new ArrayList<>();
}
