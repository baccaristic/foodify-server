package com.foodify.server.modules.orders.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OrderRequest {
    private String deliveryAddress;
    private List<OrderItemRequest> items;
    private LocationDto location;
    private String paymentMethod;
    private Long restaurantId;
    private Long userId;
}
