package com.foodify.addressesservice.addresses.api;

import com.foodify.addressesservice.addresses.application.SavedAddressService;
import com.foodify.addressesservice.addresses.dto.SaveAddressRequest;
import com.foodify.addressesservice.addresses.dto.SavedAddressResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/addresses")
@RequiredArgsConstructor
public class SavedAddressController {

    private final SavedAddressService savedAddressService;

    @PreAuthorize("hasAuthority('ROLE_CLIENT')")
    @PostMapping
    public ResponseEntity<SavedAddressResponse> createAddress(@Valid @RequestBody SaveAddressRequest request,
                                                              Authentication authentication) {
        Long userId = extractUserId(authentication);
        SavedAddressResponse response = savedAddressService.createAddress(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PreAuthorize("hasAuthority('ROLE_CLIENT')")
    @PutMapping("/{addressId}")
    public ResponseEntity<SavedAddressResponse> updateAddress(@PathVariable UUID addressId,
                                                              @Valid @RequestBody SaveAddressRequest request,
                                                              Authentication authentication) {
        Long userId = extractUserId(authentication);
        SavedAddressResponse response = savedAddressService.updateAddress(userId, addressId, request);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAuthority('ROLE_CLIENT')")
    @GetMapping("/mySavedAddresses")
    public ResponseEntity<List<SavedAddressResponse>> getMySavedAddresses(Authentication authentication) {
        Long userId = extractUserId(authentication);
        List<SavedAddressResponse> addresses = savedAddressService.getAddressesForUser(userId);
        return ResponseEntity.ok(addresses);
    }

    private Long extractUserId(Authentication authentication) {
        if (authentication == null || authentication.getPrincipal() == null) {
            throw new IllegalStateException("Authenticated user not found");
        }
        return Long.parseLong(authentication.getPrincipal().toString());
    }
}
