package com.foodify.server.modules.admin.dto;

import lombok.Data;

@Data
public class DeliverOrderDto {
    private Long orderId;
    private String token;
}
