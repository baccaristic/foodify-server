package com.foodify.server.modules.identity.api;

import com.foodify.server.modules.identity.application.DriverVerificationService;
import com.foodify.server.modules.identity.domain.DriverDocumentType;
import com.foodify.server.modules.identity.domain.User;
import com.foodify.server.modules.identity.dto.DriverDocumentReviewRequest;
import com.foodify.server.modules.identity.dto.DriverVerificationSummaryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/admin/drivers")
@RequiredArgsConstructor
public class AdminDriverVerificationController {

    private final DriverVerificationService driverVerificationService;

    @GetMapping("/{driverId}/verification")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public DriverVerificationSummaryDto driverVerification(@PathVariable Long driverId) {
        return driverVerificationService.getSummary(driverId);
    }

    @PostMapping("/{driverId}/documents/{type}/review")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public DriverVerificationSummaryDto reviewDocument(
            @PathVariable Long driverId,
            @PathVariable String type,
            @Valid @RequestBody DriverDocumentReviewRequest request,
            Authentication authentication
    ) {
        Long adminId = resolveAdminId(authentication);
        DriverDocumentType documentType = parseType(type);
        return driverVerificationService.reviewDocument(
                adminId,
                driverId,
                documentType,
                request.getStatus(),
                request.getRejectionReason()
        );
    }

    private DriverDocumentType parseType(String value) {
        try {
            return DriverDocumentType.fromValue(value);
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unsupported document type", ex);
        }
    }

    private Long resolveAdminId(Authentication authentication) {
        if (authentication == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Not authenticated");
        }
        Object principal = authentication.getPrincipal();
        if (principal instanceof User user) {
            return user.getId();
        }
        if (principal instanceof Number number) {
            return number.longValue();
        }
        if (principal instanceof String principalStr && StringUtils.hasText(principalStr)) {
            try {
                return Long.parseLong(principalStr);
            } catch (NumberFormatException ex) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid admin session", ex);
            }
        }
        String name = authentication.getName();
        if (StringUtils.hasText(name)) {
            try {
                return Long.parseLong(name);
            } catch (NumberFormatException ex) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid admin session", ex);
            }
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unable to resolve admin session");
    }
}
