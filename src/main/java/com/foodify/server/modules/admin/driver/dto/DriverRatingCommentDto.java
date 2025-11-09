package com.foodify.server.modules.admin.driver.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DriverRatingCommentDto {
    private Long ratingId;
    private Long deliveryId;
    private Long orderId;
    private String clientName;
    private Integer timingRating;
    private Integer foodConditionRating;
    private Integer professionalismRating;
    private Integer overallRating;
    private String comments;
    private LocalDateTime createdAt;
}
