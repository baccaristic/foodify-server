package com.foodify.server.modules.addresses.application;

import com.foodify.server.modules.addresses.domain.SavedAddress;
import com.foodify.server.modules.addresses.dto.SaveAddressRequest;
import com.foodify.server.modules.addresses.dto.SavedAddressResponse;
import com.foodify.server.modules.addresses.mapper.SavedAddressMapper;
import com.foodify.server.modules.addresses.repository.SavedAddressRepository;
import com.foodify.server.modules.identity.domain.User;
import com.foodify.server.modules.identity.repository.UserRepository;
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
    private final UserRepository userRepository;
    private final SavedAddressMapper savedAddressMapper;

    @Transactional
    public SavedAddressResponse createAddress(Long userId, SaveAddressRequest request) {
        User user = loadUser(userId);
        SavedAddress savedAddress = new SavedAddress();
        savedAddress.setUser(user);
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

    private User loadUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
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
