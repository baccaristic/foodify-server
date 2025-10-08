package com.foodify.addressesservice.addresses.application;

import com.foodify.addressesservice.addresses.domain.SavedAddress;

import java.util.Optional;
import java.util.UUID;

public interface SavedAddressDirectoryService {

    Optional<SavedAddress> findByIdAndClient(UUID savedAddressId, Long clientId);
}
