package com.foodify.server.modules.admin.client.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminClientDetailsDto {
    private Long id;
    private String name;
    private String phoneNumber;
    private String email;
    private List<ClientAddressDto> addresses;
}
