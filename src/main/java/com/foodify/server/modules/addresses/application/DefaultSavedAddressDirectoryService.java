package com.foodify.server.modules.addresses.application;

import com.foodify.server.modules.addresses.domain.SavedAddress;
import com.foodify.server.modules.addresses.repository.SavedAddressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DefaultSavedAddressDirectoryService implements SavedAddressDirectoryService {

    private final SavedAddressRepository savedAddressRepository;

    @Override
    public Optional<SavedAddress> findByIdAndClient(UUID savedAddressId, Long clientId) {
        if (savedAddressId == null || clientId == null) {
            return Optional.empty();
        }
        return savedAddressRepository.findByIdAndUserId(savedAddressId, clientId);
    }
}
