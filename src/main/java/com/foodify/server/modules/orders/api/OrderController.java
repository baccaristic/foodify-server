package com.foodify.server.modules.orders.api;

import com.foodify.server.modules.orders.application.CustomerOrderService;
import com.foodify.server.modules.orders.dto.OrderRequest;
import com.foodify.server.modules.orders.dto.response.CreateOrderResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final CustomerOrderService customerOrderService;

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('ROLE_CLIENT')")
    public ResponseEntity<CreateOrderResponse> createOrder(@Valid @RequestBody OrderRequest orderRequest, Authentication auth) {
        Long userId = Long.parseLong(auth.getPrincipal().toString());
        CreateOrderResponse response = customerOrderService.placeOrder(userId, orderRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
