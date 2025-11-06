package com.foodify.server.modules.admin.dto;

import lombok.Builder;
import lombok.Value;

import java.util.Set;

@Value
@Builder
public class MenuItemListDto {
    Long id;
    String name;
    String description;
    Double price;
    Boolean available;
    Set<String> categories;
    String imageUrl;
    Boolean hasPromotion;
    Double promotionPrice;
}
