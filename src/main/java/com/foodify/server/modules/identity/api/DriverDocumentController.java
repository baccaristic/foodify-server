package com.foodify.server.modules.identity.api;

import com.foodify.server.modules.identity.application.DriverVerificationService;
import com.foodify.server.modules.identity.domain.DriverDocumentType;
import com.foodify.server.modules.identity.dto.DriverVerificationSummaryDto;
import com.foodify.server.modules.identity.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/driver/documents")
@RequiredArgsConstructor
public class DriverDocumentController {

    private final DriverVerificationService driverVerificationService;

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_DRIVER')")
    public DriverVerificationSummaryDto summary(Authentication authentication) {
        Long driverId = resolveDriverId(authentication);
        return driverVerificationService.getSummary(driverId);
    }

    @PostMapping(value = "/{type}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAuthority('ROLE_DRIVER')")
    public DriverVerificationSummaryDto upload(
            @PathVariable("type") String type,
            @RequestPart("file") MultipartFile file,
            Authentication authentication
    ) {
        Long driverId = resolveDriverId(authentication);
        DriverDocumentType documentType = parseType(type);
        return driverVerificationService.uploadDocument(driverId, documentType, file);
    }

    private DriverDocumentType parseType(String value) {
        try {
            return DriverDocumentType.fromValue(value);
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unsupported document type", ex);
        }
    }

    private Long resolveDriverId(Authentication authentication) {
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
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid driver session", ex);
            }
        }
        String name = authentication.getName();
        if (StringUtils.hasText(name)) {
            try {
                return Long.parseLong(name);
            } catch (NumberFormatException ex) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid driver session", ex);
            }
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unable to resolve driver session");
    }
}
