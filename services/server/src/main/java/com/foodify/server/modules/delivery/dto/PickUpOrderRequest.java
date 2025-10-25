package com.foodify.server.modules.delivery.dto;

import lombok.Data;

@Data
public class PickUpOrderRequest {
    private String orderId;
    private String token;
}
