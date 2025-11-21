package com.foodify.server.modules.identity.dto;

import com.foodify.server.modules.identity.domain.DriverVerificationStatus;
import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class DriverVerificationSummaryDto {
    Long driverId;
    DriverVerificationStatus status;
    int totalDocuments;
    int submittedDocuments;
    int approvedDocuments;
    List<DriverDocumentDto> documents;
}
