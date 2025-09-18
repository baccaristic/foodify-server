package com.foodify.server.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OrderItemRequest {
    private Long menuItemId;
    private int quantity;
    private List<ExtraDTO> extras;
    private String specialInstructions;
}
