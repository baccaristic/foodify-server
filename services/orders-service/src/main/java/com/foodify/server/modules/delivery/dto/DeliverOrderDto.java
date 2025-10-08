package com.foodify.server.modules.delivery.dto;

import lombok.Data;

@Data
public class DeliverOrderDto {
    private Long orderId;
    private String token;
}
