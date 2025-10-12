package com.foodify.server.modules.restaurants.mapper;

import com.foodify.server.modules.restaurants.dto.RestaurantDisplayDto;
import com.foodify.server.modules.restaurants.dto.RestaurantDto;
import com.foodify.server.modules.restaurants.domain.Restaurant;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RestaurantMapper {
    Restaurant toEntity(RestaurantDto dto);
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "rating", source = "rating")
    @Mapping(target = "type", source = "type")
    @Mapping(target = "address", source = "address")
    @Mapping(target = "phone", source = "phone")
    @Mapping(target = "openingHours", source = "openingHours")
    @Mapping(target = "closingHours", source = "closingHours")
    @Mapping(target = "latitude", source = "latitude")
    @Mapping(target = "longitude", source = "longitude")
    @Mapping(target = "imageUrl", source = "imageUrl")
    @Mapping(target = "favorite", constant = "false")
    @Mapping(target = "hasPromotion", constant = "false")
    @Mapping(target = "promotionSummary", ignore = true)
    RestaurantDisplayDto toDto(Restaurant entity);
    List<RestaurantDisplayDto> toDto(List<Restaurant> entities);
}
