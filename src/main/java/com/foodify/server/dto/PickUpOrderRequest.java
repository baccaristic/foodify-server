package com.foodify.server.dto;

import lombok.Data;

@Data
public class PickUpOrderRequest {
    private String orderId;
    private String token;
}
