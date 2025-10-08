package com.foodify.server.modules.orders.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.foodify.server.modules.orders.domain.catalog.OrderItemCatalogSnapshot;
import com.foodify.server.modules.orders.domain.catalog.OrderItemExtraSnapshot;
import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class OrderItem {
    @Id @GeneratedValue
    private Long id;

    private String specialInstructions;

    @Embedded
    private OrderItemCatalogSnapshot catalogItem;

    @ElementCollection
    @CollectionTable(
            name = "order_item_extras",
            joinColumns = @JoinColumn(name = "order_item_id")
    )
    private List<OrderItemExtraSnapshot> menuItemExtras = new ArrayList<>();

    private int quantity;

    @JsonIgnore
    @ManyToOne(optional = false)
    @JoinColumn(name = "order_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Order order;
}
