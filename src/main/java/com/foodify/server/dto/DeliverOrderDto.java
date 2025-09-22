package com.foodify.server.dto;

import lombok.Data;

@Data
public class DeliverOrderDto {
    private Long orderId;
    private String token;
}
