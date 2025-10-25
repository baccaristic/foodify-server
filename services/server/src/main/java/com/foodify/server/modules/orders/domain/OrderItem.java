package com.foodify.server.modules.orders.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.foodify.server.modules.restaurants.domain.MenuItem;
import com.foodify.server.modules.restaurants.domain.MenuItemExtra;
import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Getter
@Setter
public class OrderItem {
    @Id @GeneratedValue
    private Long id;

    private String specialInstructions;

    @ManyToOne
    private MenuItem menuItem;


    @ManyToMany
    @JoinTable(
            name = "order_item_menu_item_extras",
            joinColumns = @JoinColumn(name = "order_item_id"),
            inverseJoinColumns = @JoinColumn(name = "menu_item_extras_id")
    )
    private Set<MenuItemExtra> menuItemExtras = new LinkedHashSet<>();

    private int quantity;

    @JsonIgnore
    @ManyToOne(optional = false)
    @JoinColumn(name = "order_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Order order;

    @Column(name = "unit_base_price", precision = 12, scale = 2)
    private BigDecimal unitBasePrice;

    @Column(name = "unit_price", precision = 12, scale = 2)
    private BigDecimal unitPrice;

    @Column(name = "unit_extras_price", precision = 12, scale = 2)
    private BigDecimal unitExtrasPrice;

    @Column(name = "line_subtotal", precision = 12, scale = 2)
    private BigDecimal lineSubtotal;

    @Column(name = "promotion_discount", precision = 12, scale = 2)
    private BigDecimal promotionDiscount;

    @Column(name = "line_items_total", precision = 12, scale = 2)
    private BigDecimal lineItemsTotal;

    @Column(name = "extras_total", precision = 12, scale = 2)
    private BigDecimal extrasTotal;

    @Column(name = "line_total", precision = 12, scale = 2)
    private BigDecimal lineTotal;
}
