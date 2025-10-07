package com.foodify.server.modules.addresses.application;

import com.foodify.server.modules.addresses.domain.SavedAddress;

import java.util.Optional;
import java.util.UUID;

public interface SavedAddressDirectoryService {

    Optional<SavedAddress> findByIdAndClient(UUID savedAddressId, Long clientId);
}
