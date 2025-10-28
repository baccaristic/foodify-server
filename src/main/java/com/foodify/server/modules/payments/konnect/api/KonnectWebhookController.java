package com.foodify.server.modules.payments.konnect.api;

import com.foodify.server.modules.payments.konnect.KonnectPaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RestController
@RequestMapping("/api/payments/konnect/webhook")
public class KonnectWebhookController {

    private final KonnectPaymentService paymentService;

    public KonnectWebhookController(KonnectPaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @GetMapping
    public ResponseEntity<Void> handleWebhook(@RequestParam(name = "payment_ref", required = false) String paymentRef) {
        if (!StringUtils.hasText(paymentRef)) {
            throw new ResponseStatusException(BAD_REQUEST, "payment_ref is required");
        }

        boolean processed = paymentService.handleWebhook(paymentRef);
        return processed
                ? ResponseEntity.noContent().build()
                : ResponseEntity.accepted().build();
    }
}
