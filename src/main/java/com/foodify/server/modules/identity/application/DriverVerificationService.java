package com.foodify.server.modules.identity.application;

import com.foodify.server.modules.identity.domain.Admin;
import com.foodify.server.modules.identity.domain.Driver;
import com.foodify.server.modules.identity.domain.DriverDocument;
import com.foodify.server.modules.identity.domain.DriverDocumentStatus;
import com.foodify.server.modules.identity.domain.DriverDocumentType;
import com.foodify.server.modules.identity.domain.DriverVerificationStatus;
import com.foodify.server.modules.identity.dto.DriverDocumentDto;
import com.foodify.server.modules.identity.dto.DriverDocumentState;
import com.foodify.server.modules.identity.dto.DriverVerificationSummaryDto;
import com.foodify.server.modules.identity.repository.AdminRepository;
import com.foodify.server.modules.identity.repository.DriverDocumentRepository;
import com.foodify.server.modules.identity.repository.DriverRepository;
import com.foodify.server.modules.storage.application.CloudflareImagesService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class DriverVerificationService {

    private final DriverRepository driverRepository;
    private final DriverDocumentRepository driverDocumentRepository;
    private final AdminRepository adminRepository;
    private final CloudflareImagesService cloudflareImagesService;

    @Transactional(readOnly = true)
    public DriverVerificationSummaryDto getSummary(Long driverId) {
        Driver driver = findDriver(driverId);
        List<DriverDocument> documents = driverDocumentRepository.findAllByDriver_Id(driverId);
        return buildSummary(driver, documents);
    }

    @Transactional
    public DriverVerificationSummaryDto uploadDocument(Long driverId, DriverDocumentType type, MultipartFile file) {
        if (type == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Document type is required");
        }
        if (file == null || file.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Image file is required");
        }

        Driver driver = findDriver(driverId);
        String imageUrl = uploadToCloudflare(file);

        DriverDocument document = driverDocumentRepository
                .findByDriver_IdAndType(driverId, type)
                .orElseGet(() -> {
                    DriverDocument doc = new DriverDocument();
                    doc.setDriver(driver);
                    doc.setType(type);
                    return doc;
                });

        document.setDriver(driver);
        document.setType(type);
        document.setImageUrl(imageUrl);
        document.setUploadedAt(LocalDateTime.now());
        document.setStatus(DriverDocumentStatus.PENDING_REVIEW);
        document.setReviewedAt(null);
        document.setReviewedByAdminId(null);
        document.setReviewedByAdminName(null);
        document.setRejectionReason(null);

        driverDocumentRepository.save(document);

        List<DriverDocument> documents = driverDocumentRepository.findAllByDriver_Id(driverId);
        refreshDriverStatus(driver, documents);
        return buildSummary(driver, documents);
    }

    @Transactional
    public DriverVerificationSummaryDto reviewDocument(
            Long adminId,
            Long driverId,
            DriverDocumentType type,
            DriverDocumentStatus decision,
            String rejectionReason
    ) {
        if (type == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Document type is required");
        }
        if (decision == null || decision == DriverDocumentStatus.PENDING_REVIEW) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid review decision");
        }

        Driver driver = findDriver(driverId);
        Admin admin = findAdmin(adminId);
        DriverDocument document = driverDocumentRepository
                .findByDriver_IdAndType(driverId, type)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Document has not been uploaded yet"));

        document.setStatus(decision);
        document.setReviewedAt(LocalDateTime.now());
        document.setReviewedByAdminId(admin.getId());
        document.setReviewedByAdminName(admin.getName());

        if (decision == DriverDocumentStatus.REJECTED) {
            if (!StringUtils.hasText(rejectionReason)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Rejection reason is required");
            }
            document.setRejectionReason(rejectionReason.trim());
        } else {
            document.setRejectionReason(null);
        }

        driverDocumentRepository.save(document);

        List<DriverDocument> documents = driverDocumentRepository.findAllByDriver_Id(driverId);
        refreshDriverStatus(driver, documents);
        return buildSummary(driver, documents);
    }

    private String uploadToCloudflare(MultipartFile file) {
        try {
            return cloudflareImagesService.uploadImage(file);
        } catch (IOException ex) {
            log.error("Failed to upload driver document to Cloudflare", ex);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to upload image", ex);
        }
    }

    private Driver findDriver(Long driverId) {
        return driverRepository.findById(driverId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Driver not found"));
    }

    private Admin findAdmin(Long adminId) {
        return adminRepository.findById(adminId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Admin not found"));
    }

    private void refreshDriverStatus(Driver driver, List<DriverDocument> documents) {
        DriverVerificationStatus status = determineStatus(documents);
        driver.setVerificationStatus(status);
        if (status == DriverVerificationStatus.APPROVED) {
            driver.setVerificationCompletedAt(LocalDateTime.now());
        } else {
            driver.setVerificationCompletedAt(null);
        }
        driverRepository.save(driver);
    }

    private DriverVerificationStatus determineStatus(List<DriverDocument> documents) {
        int required = DriverDocumentType.values().length;
        boolean hasRejected = documents.stream().anyMatch(doc -> doc.getStatus() == DriverDocumentStatus.REJECTED);
        boolean hasPending = documents.stream().anyMatch(doc -> doc.getStatus() == DriverDocumentStatus.PENDING_REVIEW);
        int approvedCount = (int) documents.stream()
                .filter(doc -> doc.getStatus() == DriverDocumentStatus.APPROVED)
                .count();

        if (approvedCount == required && required > 0) {
            return DriverVerificationStatus.APPROVED;
        }
        if (hasRejected) {
            return DriverVerificationStatus.REJECTED;
        }
        if (documents.size() < required) {
            return DriverVerificationStatus.PENDING_DOCUMENTS;
        }
        if (hasPending) {
            return DriverVerificationStatus.IN_REVIEW;
        }
        return DriverVerificationStatus.PENDING_DOCUMENTS;
    }

    private DriverVerificationSummaryDto buildSummary(Driver driver, List<DriverDocument> documents) {
        Map<DriverDocumentType, DriverDocument> byType = new LinkedHashMap<>();
        for (DriverDocument document : documents) {
            byType.put(document.getType(), document);
        }

        List<DriverDocumentDto> documentDtos = new ArrayList<>();
        int submitted = 0;
        int approved = 0;

        for (DriverDocumentType type : DriverDocumentType.values()) {
            DriverDocument document = byType.get(type);
            DriverDocumentState state = resolveState(document);
            if (state != DriverDocumentState.MISSING) {
                submitted++;
            }
            if (state == DriverDocumentState.APPROVED) {
                approved++;
            }
            documentDtos.add(toDto(type, document, state));
        }

        return DriverVerificationSummaryDto.builder()
                .driverId(driver.getId())
                .status(driver.getVerificationStatus())
                .totalDocuments(DriverDocumentType.values().length)
                .submittedDocuments(submitted)
                .approvedDocuments(approved)
                .documents(documentDtos)
                .build();
    }

    private DriverDocumentDto toDto(DriverDocumentType type, DriverDocument document, DriverDocumentState state) {
        return DriverDocumentDto.builder()
                .type(type)
                .title(type.getTitle())
                .instructions(type.getInstructions())
                .state(state)
                .imageUrl(document != null ? document.getImageUrl() : null)
                .rejectionReason(document != null ? document.getRejectionReason() : null)
                .uploadedAt(document != null ? document.getUploadedAt() : null)
                .reviewedAt(document != null ? document.getReviewedAt() : null)
                .reviewedBy(document != null ? document.getReviewedByAdminName() : null)
                .build();
    }

    private DriverDocumentState resolveState(DriverDocument document) {
        if (document == null) {
            return DriverDocumentState.MISSING;
        }
        return switch (document.getStatus()) {
            case APPROVED -> DriverDocumentState.APPROVED;
            case REJECTED -> DriverDocumentState.REJECTED;
            case PENDING_REVIEW -> DriverDocumentState.PENDING_REVIEW;
        };
    }
}
