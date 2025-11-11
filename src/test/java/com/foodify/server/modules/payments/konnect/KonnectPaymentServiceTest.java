package com.foodify.server.modules.payments.konnect;

import com.foodify.server.modules.identity.domain.Client;
import com.foodify.server.modules.identity.domain.RestaurantAdmin;
import com.foodify.server.modules.notifications.websocket.WebSocketService;
import com.foodify.server.modules.orders.domain.Order;
import com.foodify.server.modules.orders.repository.OrderRepository;
import com.foodify.server.modules.payments.konnect.dto.KonnectInitiatePaymentRequest;
import com.foodify.server.modules.payments.konnect.dto.KonnectInitiatePaymentResponse;
import com.foodify.server.modules.payments.konnect.dto.KonnectPaymentDetailsResponse;
import com.foodify.server.modules.payments.konnect.exception.KonnectApiException;
import com.foodify.server.modules.restaurants.domain.Restaurant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class KonnectPaymentServiceTest {

    @Mock
    private KonnectApiClient apiClient;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private WebSocketService webSocketService;

    private KonnectProperties properties;
    private KonnectPaymentService paymentService;

    @BeforeEach
    void setUp() {
        properties = new KonnectProperties();
        properties.setEnabled(true);
        properties.setEnvironment("sandbox");
        properties.getSandbox().setBaseUrl("https://sandbox.example/api/v2");
        properties.getSandbox().setApiKey("sandbox-key");
        properties.getSandbox().setReceiverWalletId("wallet-123");
        properties.getSandbox().setWebhook("https://example.test/webhook");
        KonnectProperties.PaymentOptions payment = properties.getPayment();
        payment.setAcceptedMethods(List.of("bank_card"));
        payment.setLifespanMinutes(45);
        payment.setAmountScale(1000);
        payment.setDescriptionTemplate("Order {orderId} at {restaurant}");
        paymentService = new KonnectPaymentService(properties, apiClient, orderRepository, webSocketService);
    }

    @Test
    void initiatePaymentBuildsRequestWithConvertedAmount() {
        Order order = new Order();
        order.setId(42L);
        order.setTotal(new BigDecimal("19.75"));
        order.setPaymentMethod("card");
        order.setRestaurant(new com.foodify.server.modules.restaurants.domain.Restaurant());
        order.getRestaurant().setName("Pizza Hub");

        Client client = new Client();
        client.setName("Jane Doe");
        client.setEmail("jane@example.test");
        client.setPhoneNumber(" 22 222 222 ");

        when(apiClient.initiatePayment(any(), any(), any()))
                .thenReturn(new KonnectInitiatePaymentResponse("https://pay.example/abc", "ref-123"));

        LocalDateTime before = LocalDateTime.now();
        var result = paymentService.initiatePayment(order, client);

        assertThat(result).isPresent();
        KonnectPaymentService.KonnectPaymentSummary summary = result.orElseThrow();
        assertThat(summary.paymentUrl()).isEqualTo("https://pay.example/abc");
        assertThat(summary.paymentReference()).isEqualTo("ref-123");
        assertThat(summary.status()).isEqualTo("pending");
        assertThat(summary.environment()).isEqualTo("sandbox");
        assertThat(summary.expiresAt()).isNotNull();
        assertThat(Duration.between(before.plusMinutes(45), summary.expiresAt()).abs().toMinutes()).isLessThanOrEqualTo(1);

        ArgumentCaptor<KonnectInitiatePaymentRequest> requestCaptor = ArgumentCaptor.forClass(KonnectInitiatePaymentRequest.class);
        verify(apiClient).initiatePayment(eq("https://sandbox.example/api/v2"), eq("sandbox-key"), requestCaptor.capture());
        KonnectInitiatePaymentRequest request = requestCaptor.getValue();
        assertThat(request.receiverWalletId()).isEqualTo("wallet-123");
        assertThat(request.amount()).isEqualTo(19750L);
        assertThat(request.description()).isEqualTo("Order 42 at Pizza Hub");
        assertThat(request.acceptedPaymentMethods()).containsExactly("bank_card");
        assertThat(request.firstName()).isEqualTo("Jane");
        assertThat(request.lastName()).isEqualTo("Doe");
        assertThat(request.phoneNumber()).isEqualTo("22222222");
        assertThat(request.email()).isEqualTo("jane@example.test");
        assertThat(request.orderId()).isEqualTo("42");
        assertThat(request.webhook()).isEqualTo("https://example.test/webhook");
    }

    @Test
    void initiatePaymentSkipsWhenDisabled() {
        properties.setEnabled(false);
        paymentService = new KonnectPaymentService(properties, apiClient, orderRepository, webSocketService);

        Order order = new Order();
        order.setPaymentMethod("card");

        assertThat(paymentService.initiatePayment(order, null)).isEmpty();
        verify(apiClient, never()).initiatePayment(any(), any(), any());
    }

    @Test
    void initiatePaymentSkipsWhenEnvironmentIncomplete() {
        properties.getSandbox().setApiKey(null);
        paymentService = new KonnectPaymentService(properties, apiClient, orderRepository, webSocketService);

        Order order = new Order();
        order.setPaymentMethod("card");

        assertThat(paymentService.initiatePayment(order, null)).isEmpty();
        verify(apiClient, never()).initiatePayment(any(), any(), any());
    }

    @Test
    void initiatePaymentPropagatesGatewayErrors() {
        Order order = new Order();
        order.setId(10L);
        order.setTotal(new BigDecimal("12.00"));
        order.setPaymentMethod("card");

        when(apiClient.initiatePayment(any(), any(), any()))
                .thenThrow(new KonnectApiException("boom"));

        assertThatThrownBy(() -> paymentService.initiatePayment(order, null))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Unable to initiate payment with Konnect");
    }

    @Test
    void handleWebhookUpdatesOrderAndNotifiesParties() {
        Order order = new Order();
        order.setId(84L);
        Client client = new Client();
        client.setId(11L);
        order.setClient(client);
        Restaurant restaurant = new Restaurant();
        restaurant.setId(51L);
        RestaurantAdmin admin = new RestaurantAdmin();
        admin.setId(99L);
        restaurant.setAdmin(admin);
        order.setRestaurant(restaurant);

        when(orderRepository.findById(84L)).thenReturn(java.util.Optional.of(order));
        when(apiClient.getPaymentDetails(any(), any(), eq("ref-123")))
                .thenReturn(new KonnectPaymentDetailsResponse(
                        new KonnectPaymentDetailsResponse.Payment(
                                "87a4c597cb3c15311bdbd547",
                                "completed",
                                1000L,
                                0L,
                                1000L,
                                "TND",
                                "https://pay.example/ref-123",
                                null,
                                "84",
                                "immediate",
                                "2024-05-10T12:00:00Z"
                        )
                ));
        when(orderRepository.save(order)).thenReturn(order);

        boolean processed = paymentService.handleWebhook("ref-123");

        assertThat(processed).isTrue();
        assertThat(order.getPaymentStatus()).isEqualTo("paid");
        assertThat(order.getPaymentUrl()).isEqualTo("https://pay.example/ref-123");
        assertThat(order.getPaymentEnvironment()).isEqualTo("sandbox");
        assertThat(order.getPaymentExpiresAt()).isNotNull();

        verify(orderRepository).findById(84L);
        verify(orderRepository, never()).findByPaymentReference(any());
        verify(orderRepository).save(order);
        verify(webSocketService).notifyClient(11L, order);
        verify(webSocketService).notifyRestaurant(51L, order);
    }

    @Test
    void handleWebhookFallsBackToPaymentReferenceWhenOrderIdMissing() {
        Order order = new Order();
        order.setId(99L);
        when(orderRepository.findByPaymentReference("ref-789"))
                .thenReturn(java.util.Optional.of(order));
        when(apiClient.getPaymentDetails(any(), any(), eq("ref-789")))
                .thenReturn(new KonnectPaymentDetailsResponse(
                        new KonnectPaymentDetailsResponse.Payment(
                                "ref-789",
                                "pending",
                                500L,
                                500L,
                                0L,
                                "TND",
                                null,
                                null,
                                null,
                                null,
                                null
                        )
                ));
        when(orderRepository.save(order)).thenReturn(order);

        boolean processed = paymentService.handleWebhook("ref-789");

        assertThat(processed).isTrue();
        verify(orderRepository).findByPaymentReference("ref-789");
        verify(orderRepository).save(order);
        assertThat(order.getPaymentReference()).isEqualTo("ref-789");
    }

    @Test
    void handleWebhookSkipsWhenEnvironmentIncomplete() {
        properties.getSandbox().setApiKey(null);
        paymentService = new KonnectPaymentService(properties, apiClient, orderRepository, webSocketService);

        boolean processed = paymentService.handleWebhook("ref-123");

        assertThat(processed).isFalse();
        verifyNoInteractions(apiClient, orderRepository, webSocketService);
    }

    @Test
    void handleWebhookReturnsFalseWhenOrderMissing() {
        when(apiClient.getPaymentDetails(any(), any(), eq("ref-unknown")))
                .thenReturn(new KonnectPaymentDetailsResponse(
                        new KonnectPaymentDetailsResponse.Payment(
                                "ref-unknown",
                                "pending",
                                1000L,
                                1000L,
                                0L,
                                "TND",
                                null,
                                null,
                                null,
                                null,
                                null
                        )
                ));
        when(orderRepository.findByPaymentReference("ref-unknown"))
                .thenReturn(java.util.Optional.empty());

        boolean processed = paymentService.handleWebhook("ref-unknown");

        assertThat(processed).isFalse();
        verify(orderRepository).findByPaymentReference("ref-unknown");
        verify(orderRepository, never()).findById(any());
        verifyNoInteractions(webSocketService);
    }

    @Test
    void handleWebhookPropagatesApiErrors() {
        Order order = new Order();
        order.setId(99L);
        when(orderRepository.findByPaymentReference("ref-error"))
                .thenReturn(java.util.Optional.of(order));
        when(apiClient.getPaymentDetails(any(), any(), eq("ref-error")))
                .thenThrow(new KonnectApiException("boom"));

        assertThatThrownBy(() -> paymentService.handleWebhook("ref-error"))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Unable to fetch payment from Konnect");
    }
}
