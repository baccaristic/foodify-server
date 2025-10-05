package com.foodify.server.modules.restaurants.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.foodify.server.modules.identity.domain.RestaurantAdmin;
import com.foodify.server.modules.orders.domain.Order;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Restaurant {
    @Id @GeneratedValue
    private Long id;
    private String name;
    private String address;
    private String phone;
    private String type;
    private Double rating;
    private String openingHours;
    private String closingHours;
    private String description;
    private String licenseNumber;
    private String taxId;
    private double latitude;
    private double longitude;
    private String imageUrl;
    @Column(name = "top_choice")
    private Boolean topChoice = Boolean.FALSE;
    @Column(name = "free_delivery")
    private Boolean freeDelivery = Boolean.FALSE;
    @Column(name = "top_eat")
    private Boolean topEat = Boolean.FALSE;
    @Column(name = "promotion_label")
    private String promotionLabel;
    @Column(name = "delivery_fee")
    private Double deliveryFee;
    @Column(name = "delivery_time_range")
    private String deliveryTimeRange;

    @OneToOne
    @JoinColumn(name = "admin_id")
    private RestaurantAdmin admin;

    @OneToMany(mappedBy = "restaurant")
    private List<MenuItem> menu;

    @OneToMany(mappedBy = "restaurant")
    private List<Order> orders;
}