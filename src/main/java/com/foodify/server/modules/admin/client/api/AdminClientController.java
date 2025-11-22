package com.foodify.server.modules.admin.client.api;

import com.foodify.server.modules.admin.client.application.AdminClientService;
import com.foodify.server.modules.admin.client.dto.AdminClientDetailsDto;
import com.foodify.server.modules.admin.client.dto.AdminClientDto;
import com.foodify.server.modules.admin.client.dto.ClientFilterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/admin/clients")
@RequiredArgsConstructor
public class AdminClientController {

    private final AdminClientService adminClientService;

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Page<AdminClientDto>> getClients(
            @RequestParam(required = false) String query,
            @RequestParam(required = false) BigDecimal minDebt,
            @RequestParam(required = false) BigDecimal maxDebt,
            @RequestParam(required = false) BigDecimal minPoints,
            @RequestParam(required = false) BigDecimal maxPoints,
            @RequestParam(required = false) Boolean active,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        ClientFilterRequest filters = ClientFilterRequest.builder()
                .query(query)
                .minDebt(minDebt)
                .maxDebt(maxDebt)
                .minPoints(minPoints)
                .maxPoints(maxPoints)
                .active(active)
                .build();

        Page<AdminClientDto> clients = adminClientService.getClients(filters, page, size);
        return ResponseEntity.ok(clients);
    }

    @GetMapping("/{clientId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<AdminClientDetailsDto> getClientDetails(@PathVariable Long clientId) {
        AdminClientDetailsDto clientDetails = adminClientService.getClientDetails(clientId);
        return ResponseEntity.ok(clientDetails);
    }
}
