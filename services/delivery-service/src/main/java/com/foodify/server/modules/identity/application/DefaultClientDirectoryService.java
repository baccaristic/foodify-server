package com.foodify.server.modules.identity.application;

import com.foodify.server.modules.identity.domain.Client;
import com.foodify.server.modules.identity.repository.ClientRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DefaultClientDirectoryService implements ClientDirectoryService {

    private final ClientRepository clientRepository;

    @Override
    public Client getClientOrThrow(Long clientId) {
        if (clientId == null) {
            throw new IllegalArgumentException("Client id is required");
        }
        return clientRepository.findById(clientId)
                .orElseThrow(() -> new EntityNotFoundException("Client not found"));
    }
}
