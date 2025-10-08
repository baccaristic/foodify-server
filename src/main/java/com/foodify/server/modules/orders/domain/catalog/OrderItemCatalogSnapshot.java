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
public class OrderItemCatalogSnapshot {

    private Long menuItemId;
    private String menuItemName;
    private Double basePrice;
    private Boolean promotionActive;
    private Double promotionPrice;
}
