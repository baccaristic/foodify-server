package com.foodify.server.modules.admin.driver.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RatingDistributionDto {
    private Long driverId;
    private List<Long> distribution; // [1-star count, 2-star count, 3-star count, 4-star count, 5-star count]
}
