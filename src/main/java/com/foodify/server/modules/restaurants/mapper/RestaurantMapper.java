package com.foodify.server.modules.restaurants.mapper;

import com.foodify.server.modules.admin.restaurant.dto.AddRestaurantDto;
import com.foodify.server.modules.restaurants.dto.RestaurantBasicInfoDto;
import com.foodify.server.modules.restaurants.dto.RestaurantDisplayDto;
import com.foodify.server.modules.restaurants.dto.RestaurantDto;
import com.foodify.server.modules.restaurants.domain.Restaurant;
import com.foodify.server.modules.restaurants.domain.RestaurantCategory;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface RestaurantMapper {
    Restaurant toEntity(RestaurantDto dto);
    
    Restaurant toEntity(AddRestaurantDto dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "admin", ignore = true)
    @Mapping(target = "imageUrl", ignore = true)
    @Mapping(target = "iconUrl", ignore = true)
    @Mapping(target = "commissionRate", ignore = true)
    @Mapping(target = "categories", ignore = true)
    @Mapping(target = "menuCategories", ignore = true)
    @Mapping(target = "menu", ignore = true)
    @Mapping(target = "orders", ignore = true)
    @Mapping(target = "operatingHours", ignore = true)
    @Mapping(target = "specialDays", ignore = true)
    void updateEntity(RestaurantDto dto, @MappingTarget Restaurant restaurant);
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "rating", source = "rating")
    @Mapping(target = "categories", source = "categories")
    @Mapping(target = "address", source = "address")
    @Mapping(target = "phone", source = "phone")
    @Mapping(target = "openingHours", source = "openingHours")
    @Mapping(target = "closingHours", source = "closingHours")
    @Mapping(target = "latitude", source = "latitude")
    @Mapping(target = "longitude", source = "longitude")
    @Mapping(target = "imageUrl", source = "imageUrl")
    @Mapping(target = "iconUrl", source = "iconUrl")
    @Mapping(target = "favorite", constant = "false")
    @Mapping(target = "hasPromotion", constant = "false")
    @Mapping(target = "promotionSummary", ignore = true)
    @Mapping(target = "sponsored", source = "sponsored")
    @Mapping(target = "position", source = "position")
    RestaurantDisplayDto toDto(Restaurant entity);
    List<RestaurantDisplayDto> toDto(List<Restaurant> entities);

    @Mapping(target = "images", ignore = true)
    RestaurantBasicInfoDto toBasicInfoDto(Restaurant entity);

    default Set<RestaurantCategory> mapCategories(Set<RestaurantCategory> categories) {
        return categories == null || categories.isEmpty() ? Set.of() : Set.copyOf(categories);
    }
}
