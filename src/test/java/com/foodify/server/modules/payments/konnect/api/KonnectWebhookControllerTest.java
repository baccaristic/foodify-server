package com.foodify.server.modules.payments.konnect.api;

import com.foodify.server.modules.payments.konnect.KonnectPaymentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(KonnectWebhookController.class)
class KonnectWebhookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private KonnectPaymentService paymentService;

    @Test
    void requiresPaymentReference() throws Exception {
        mockMvc.perform(get("/api/payments/konnect/webhook"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void returnsNoContentWhenProcessed() throws Exception {
        given(paymentService.handleWebhook(eq("ref-123"))).willReturn(true);

        mockMvc.perform(get("/api/payments/konnect/webhook")
                        .param("payment_ref", "ref-123")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void returnsAcceptedWhenNoUpdateNeeded() throws Exception {
        given(paymentService.handleWebhook(eq("ref-123"))).willReturn(false);

        mockMvc.perform(get("/api/payments/konnect/webhook")
                        .param("payment_ref", "ref-123"))
                .andExpect(status().isAccepted());
    }

    @Test
    void propagatesServiceErrors() throws Exception {
        given(paymentService.handleWebhook(eq("ref-500")))
                .willThrow(new ResponseStatusException(org.springframework.http.HttpStatus.BAD_GATEWAY, "boom"));

        mockMvc.perform(get("/api/payments/konnect/webhook")
                        .param("payment_ref", "ref-500"))
                .andExpect(status().isBadGateway());
    }
}
