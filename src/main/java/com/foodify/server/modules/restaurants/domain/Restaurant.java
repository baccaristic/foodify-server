package com.foodify.server.modules.restaurants.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.foodify.server.modules.identity.domain.RestaurantAdmin;
import com.foodify.server.modules.orders.domain.Order;
import jakarta.persistence.*;
import jakarta.persistence.Index;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(
        name = "restaurant",
        indexes = {
                @Index(name = "idx_restaurant_lat_lng", columnList = "latitude, longitude"),
                @Index(name = "idx_restaurant_top_choice", columnList = "top_choice, latitude, longitude"),
                @Index(name = "idx_restaurant_free_delivery", columnList = "free_delivery, latitude, longitude")
        }
)
@Getter
@Setter
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Restaurant {
    @Id @GeneratedValue
    private Long id;
    private String name;
    private String address;
    private String phone;
    private Double rating;
    private String openingHours;
    private String closingHours;
    private String description;
    
    // Multi-language support
    @Column(name = "name_en")
    private String nameEn;
    @Column(name = "name_fr")
    private String nameFr;
    @Column(name = "name_ar")
    private String nameAr;
    @Column(name = "description_en")
    private String descriptionEn;
    @Column(name = "description_fr")
    private String descriptionFr;
    @Column(name = "description_ar")
    private String descriptionAr;
    
    private String licenseNumber;
    private String taxId;
    private double latitude;
    private double longitude;
    private String imageUrl;

    @Column(name = "icon_url")
    private String iconUrl;
    @Column(name = "top_choice")
    private Boolean topChoice = Boolean.FALSE;
    @Column(name = "free_delivery")
    private Boolean freeDelivery = Boolean.FALSE;
    @Column(name = "top_eat")
    private Boolean topEat = Boolean.FALSE;
    @Column(name = "delivery_fee")
    private Double deliveryFee;
    @Column(name = "delivery_time_range")
    private String deliveryTimeRange;

    /**
     * Restaurant's commission rate (x) where x > 18%.
     * This represents the restaurant's margin.
     * Foodify's actual share is (x - 12)% of the order plus service fee,
     * since drivers get 12% commission of the order total (excluding delivery fee).
     */
    @Column(name = "restaurant_share_rate", precision = 5, scale = 4, nullable = true)
    private BigDecimal commissionRate = BigDecimal.valueOf(0.20).setScale(4, RoundingMode.HALF_UP);

    @OneToOne
    @JoinColumn(name = "admin_id")
    private RestaurantAdmin admin;

    @OneToMany(mappedBy = "restaurant")
    private List<MenuItem> menu;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "restaurant_category",
            joinColumns = @JoinColumn(name = "restaurant_id"),
            indexes = @Index(name = "idx_restaurant_category_category", columnList = "category")
    )
    @Column(name = "category")
    @Enumerated(EnumType.STRING)
    private Set<RestaurantCategory> categories = new HashSet<>();

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<MenuCategory> menuCategories;

    @OneToMany(mappedBy = "restaurant")
    private List<Order> orders;

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<RestaurantWeeklyOperatingHour> operatingHours = new HashSet<>();

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<RestaurantSpecialDay> specialDays = new HashSet<>();

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<RestaurantRating> ratings = new HashSet<>();
}
