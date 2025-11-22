package com.foodify.server.modules.admin.restaurant.dto;

import com.foodify.server.modules.restaurants.domain.RestaurantCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RestaurantDto {
    private Long id;
    private String name;
    private String imageUrl;
    private Set<RestaurantCategory> categories;
    private Double rating;
    private DayScheduleDto todaySchedule;
}
