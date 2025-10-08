package com.foodify.server.modules.orders.domain.catalog;

import jakarta.persistence.Column;
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
public class OrderItemExtraSnapshot {

    @Column(name = "extra_id")
    private Long extraId;
    private String name;
    private Double price;
}
