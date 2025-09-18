package com.foodify.server.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
    private String rating;
    private String openingHours;
    private String closingHours;
    private String description;
    private String licenseNumber;
    private String taxId;
    private double latitude;
    private double longitude;
    private String imageUrl;

    @OneToOne
    @JoinColumn(name = "admin_id")
    private RestaurantAdmin admin;

    @OneToMany(mappedBy = "restaurant")
    private List<MenuItem> menu;

    @OneToMany(mappedBy = "restaurant")
    private List<Order> orders;
}