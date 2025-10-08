package com.foodify.addressesservice.addresses.application;

import com.foodify.addressesservice.addresses.domain.SavedAddress;
import com.foodify.addressesservice.addresses.dto.SaveAddressRequest;
import com.foodify.addressesservice.addresses.dto.SavedAddressResponse;
import com.foodify.addressesservice.addresses.mapper.SavedAddressMapper;
import com.foodify.addressesservice.addresses.repository.SavedAddressRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SavedAddressService {

    private final SavedAddressRepository savedAddressRepository;
    private final SavedAddressMapper savedAddressMapper;

    @Transactional
    public SavedAddressResponse createAddress(Long userId, SaveAddressRequest request) {
        SavedAddress savedAddress = new SavedAddress();
        savedAddress.setUserId(userId);
        savedAddressMapper.updateEntityFromRequest(savedAddress, request);
        SavedAddress persisted = savedAddressRepository.save(savedAddress);
        handlePrimaryFlag(userId, persisted.getPrimary(), persisted.getId());
        return savedAddressMapper.toResponse(persisted);
    }

    @Transactional
    public SavedAddressResponse updateAddress(Long userId, UUID addressId, SaveAddressRequest request) {
        SavedAddress existing = savedAddressRepository.findByIdAndUserId(addressId, userId)
                .orElseThrow(() -> new EntityNotFoundException("Address not found"));

        savedAddressMapper.updateEntityFromRequest(existing, request);
        SavedAddress persisted = savedAddressRepository.save(existing);
        handlePrimaryFlag(userId, persisted.getPrimary(), persisted.getId());
        return savedAddressMapper.toResponse(persisted);
    }

    @Transactional(readOnly = true)
    public List<SavedAddressResponse> getAddressesForUser(Long userId) {
        return savedAddressRepository.findAllByUserIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(savedAddressMapper::toResponse)
                .collect(Collectors.toList());
    }

    private void handlePrimaryFlag(Long userId, Boolean isPrimary, UUID currentAddressId) {
        if (Boolean.TRUE.equals(isPrimary)) {
            List<SavedAddress> addresses = savedAddressRepository.findAllByUserIdOrderByCreatedAtDesc(userId);
            for (SavedAddress address : addresses) {
                if (Boolean.TRUE.equals(address.getPrimary())
                        && (currentAddressId == null || !currentAddressId.equals(address.getId()))) {
                    address.setPrimary(false);
                }
            }
            savedAddressRepository.saveAll(addresses);
        }
    }
}
