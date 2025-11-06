package com.foodify.server.modules.restaurants.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class TopSellingItemDto {
    Long itemId;
    String itemName;
    Long orderCount;
    String imageUrl;
    Double price;
}
