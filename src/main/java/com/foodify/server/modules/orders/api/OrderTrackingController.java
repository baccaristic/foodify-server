package com.foodify.server.modules.orders.api;

import com.foodify.server.modules.orders.readmodel.OrderTrackingQueryService;
import com.foodify.server.modules.orders.readmodel.OrderTrackingView;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@ConditionalOnBean(OrderTrackingQueryService.class)
public class OrderTrackingController {

    private final OrderTrackingQueryService orderTrackingQueryService;

    @GetMapping("/{orderId}/tracking")
    @PreAuthorize("hasAuthority('ROLE_CLIENT')")
    public ResponseEntity<OrderTrackingView> getOrderTracking(@PathVariable Long orderId,
                                                              Authentication authentication) {
        Long userId = Long.parseLong(authentication.getPrincipal().toString());
        Optional<OrderTrackingView> view = orderTrackingQueryService.find(orderId)
                .filter(trackingView -> trackingView.clientId() != null
                        && trackingView.clientId().equals(userId));
        return view.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
