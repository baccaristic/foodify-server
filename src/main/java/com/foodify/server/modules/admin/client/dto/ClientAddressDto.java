package com.foodify.server.modules.admin.client.dto;

import com.foodify.server.modules.addresses.domain.AddressType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClientAddressDto {
    private UUID id;
    private AddressType type;
    private String formattedAddress;
    private boolean primary;
}
