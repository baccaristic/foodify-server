package com.foodify.server.modules.identity.dto;

import com.foodify.server.modules.identity.domain.DriverDocumentStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DriverDocumentReviewRequest {
    @NotNull
    private DriverDocumentStatus status;
    private String rejectionReason;
}
