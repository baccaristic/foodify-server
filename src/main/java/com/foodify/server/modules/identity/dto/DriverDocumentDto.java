package com.foodify.server.modules.identity.dto;

import com.foodify.server.modules.identity.domain.DriverDocumentType;
import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;

@Value
@Builder
public class DriverDocumentDto {
    DriverDocumentType type;
    String title;
    String instructions;
    DriverDocumentState state;
    String imageUrl;
    String rejectionReason;
    LocalDateTime uploadedAt;
    LocalDateTime reviewedAt;
    String reviewedBy;
}
