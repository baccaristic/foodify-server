package com.foodify.server.modules.admin.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class RatingDistributionDto {
    Long oneStarCount;
    Long twoStarCount;
    Long threeStarCount;
    Long fourStarCount;
    Long fiveStarCount;
    Long totalRatings;
}
