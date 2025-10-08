package com.foodify.server.modules.orders.domain.catalog;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class OrderRestaurantSnapshot {

    private Long id;
    private Long adminId;
    private String name;
    private String address;
    private String phone;
    private String imageUrl;
    private Double latitude;
    private Double longitude;
}
