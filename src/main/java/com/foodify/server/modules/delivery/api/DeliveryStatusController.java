package com.foodify.server.modules.delivery.api;

import com.foodify.server.modules.delivery.application.DeliveryNetworkStatusService;
import com.foodify.server.modules.delivery.dto.DeliveryNetworkStatusDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/delivery")
@RequiredArgsConstructor
public class DeliveryStatusController {

    private final DeliveryNetworkStatusService deliveryNetworkStatusService;

    @GetMapping("/status")
    public DeliveryNetworkStatusDto getNetworkStatus() {
        return deliveryNetworkStatusService.getNetworkStatus();
    }
}
