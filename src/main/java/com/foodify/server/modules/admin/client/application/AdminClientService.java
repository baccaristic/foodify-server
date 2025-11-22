package com.foodify.server.modules.admin.client.application;

import com.foodify.server.modules.admin.client.dto.AdminClientDetailsDto;
import com.foodify.server.modules.admin.client.dto.AdminClientDto;
import com.foodify.server.modules.admin.client.dto.ClientAddressDto;
import com.foodify.server.modules.admin.client.dto.ClientFilterRequest;
import com.foodify.server.modules.admin.client.repository.AdminClientRepository;
import com.foodify.server.modules.admin.client.repository.ClientSpecifications;
import com.foodify.server.modules.addresses.domain.SavedAddress;
import com.foodify.server.modules.identity.domain.Client;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminClientService {

    private final AdminClientRepository clientRepository;

    @Transactional(readOnly = true)
    public Page<AdminClientDto> getClients(ClientFilterRequest filters, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
        
        Page<Client> clientsPage = clientRepository.findAll(
                ClientSpecifications.withFilters(
                        filters.getQuery(),
                        filters.getMinPoints(),
                        filters.getMaxPoints(),
                        filters.getActive()
                ),
                pageable
        );

        return clientsPage.map(this::mapToDto);
    }

    @Transactional(readOnly = true)
    public AdminClientDetailsDto getClientDetails(Long clientId) {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new EntityNotFoundException("Client not found with id: " + clientId));
        
        List<ClientAddressDto> addresses = client.getSavedAddresses().stream()
                .map(this::mapToAddressDto)
                .toList();
        
        return AdminClientDetailsDto.builder()
                .id(client.getId())
                .name(client.getName())
                .phoneNumber(client.getPhoneNumber())
                .email(client.getEmail())
                .addresses(addresses)
                .build();
    }

    private AdminClientDto mapToDto(Client client) {
        return AdminClientDto.builder()
                .id(client.getId())
                .name(client.getName())
                .phoneNumber(client.getPhoneNumber())
                .email(client.getEmail())
                .points(client.getLoyaltyPointsBalance() != null ? client.getLoyaltyPointsBalance() : BigDecimal.ZERO)
                .active(client.isEnabled())
                .debt(BigDecimal.ZERO)
                .build();
    }

    private ClientAddressDto mapToAddressDto(SavedAddress address) {
        return ClientAddressDto.builder()
                .id(address.getId())
                .type(address.getType())
                .formattedAddress(address.getFormattedAddress())
                .primary(Boolean.TRUE.equals(address.getPrimary()))
                .build();
    }
}
