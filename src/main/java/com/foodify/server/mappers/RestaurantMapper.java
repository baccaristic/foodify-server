package com.foodify.server.mappers;

import com.foodify.server.dto.RestaurantDisplayDto;
import com.foodify.server.dto.RestaurantDto;
import com.foodify.server.models.Restaurant;
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
    RestaurantDisplayDto toDto(Restaurant entity);
    List<RestaurantDisplayDto> toDto(List<Restaurant> entities);
}
