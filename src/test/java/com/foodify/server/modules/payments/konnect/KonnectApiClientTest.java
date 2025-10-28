package com.foodify.server.modules.payments.konnect;

import com.foodify.server.modules.payments.konnect.dto.KonnectInitiatePaymentRequest;
import com.foodify.server.modules.payments.konnect.dto.KonnectPaymentDetailsResponse;
import com.foodify.server.modules.payments.konnect.exception.KonnectApiException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.header;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

class KonnectApiClientTest {

    private RestTemplate restTemplate;
    private MockRestServiceServer server;
    private KonnectApiClient client;

    @BeforeEach
    void setUp() {
        restTemplate = new RestTemplate();
        server = MockRestServiceServer.bindTo(restTemplate).ignoreExpectOrder(true).build();
        client = new KonnectApiClient(restTemplate);
    }

    @Test
    void initiatePaymentPostsPayload() {
        server.expect(requestTo("https://api.example.com/api/v2/payments/init-payment"))
                .andExpect(method(HttpMethod.POST))
                .andExpect(header("x-api-key", "api-key"))
                .andExpect(content().json("{" +
                        "\"receiverWalletId\":\"wallet\"," +
                        "\"token\":\"TND\"," +
                        "\"amount\":1000," +
                        "\"type\":\"immediate\"," +
                        "\"description\":\"desc\"," +
                        "\"acceptedPaymentMethods\":[\"bank_card\"]" +
                        "}"))
                .andRespond(withSuccess("{\"payUrl\":\"https://pay\",\"paymentRef\":\"ref\"}", MediaType.APPLICATION_JSON));

        KonnectInitiatePaymentRequest request = new KonnectInitiatePaymentRequest(
                "wallet",
                "TND",
                1000L,
                "immediate",
                "desc",
                List.of("bank_card"),
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                "123",
                "https://webhook",
                "light"
        );

        var response = client.initiatePayment("https://api.example.com/api/v2", "api-key", request);
        assertThat(response.payUrl()).isEqualTo("https://pay");
        assertThat(response.paymentRef()).isEqualTo("ref");
        server.verify();
    }

    @Test
    void initiatePaymentThrowsOnErrorStatus() {
        server.expect(requestTo("https://api.example.com/api/v2/payments/init-payment"))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withStatus(HttpStatus.BAD_REQUEST));

        KonnectInitiatePaymentRequest request = new KonnectInitiatePaymentRequest(
                "wallet",
                "TND",
                1000L,
                "immediate",
                "desc",
                List.of("bank_card"),
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null
        );

        assertThatThrownBy(() -> client.initiatePayment("https://api.example.com/api/v2", "api-key", request))
                .isInstanceOf(KonnectApiException.class);
        server.verify();
    }

    @Test
    void getPaymentDetailsFetchesPayment() {
        server.expect(requestTo("https://api.example.com/api/v2/payments/ref-123"))
                .andExpect(method(HttpMethod.GET))
                .andExpect(header("x-api-key", "api-key"))
                .andRespond(withSuccess("{" +
                        "\"payment\":{" +
                        "\"status\":\"completed\"," +
                        "\"link\":\"https://pay.example/ref-123\"," +
                        "\"expirationDate\":\"2024-05-10T12:00:00Z\"}" +
                        "}", MediaType.APPLICATION_JSON));

        KonnectPaymentDetailsResponse response = client.getPaymentDetails(
                "https://api.example.com/api/v2",
                "api-key",
                "ref-123"
        );

        assertThat(response.payment().status()).isEqualTo("completed");
        assertThat(response.payment().link()).isEqualTo("https://pay.example/ref-123");
        assertThat(response.payment().expirationDate()).isEqualTo("2024-05-10T12:00:00Z");
        server.verify();
    }

    @Test
    void getPaymentDetailsThrowsOnError() {
        server.expect(requestTo("https://api.example.com/api/v2/payments/ref-456"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.NOT_FOUND));

        assertThatThrownBy(() -> client.getPaymentDetails(
                "https://api.example.com/api/v2",
                "api-key",
                "ref-456"))
                .isInstanceOf(KonnectApiException.class);
        server.verify();
    }
}
