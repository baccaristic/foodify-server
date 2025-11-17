package com.foodify.server.modules.payments.points.application;

import com.foodify.server.modules.delivery.application.QrCodeService;
import com.foodify.server.modules.identity.domain.Client;
import com.foodify.server.modules.identity.repository.ClientRepository;
import com.foodify.server.modules.notifications.websocket.WebSocketService;
import com.foodify.server.modules.payments.points.domain.PointsPayment;
import com.foodify.server.modules.payments.points.domain.PointsPaymentStatus;
import com.foodify.server.modules.payments.points.dto.CreatePointsPaymentRequest;
import com.foodify.server.modules.payments.points.dto.PointsPaymentResponse;
import com.foodify.server.modules.payments.points.dto.ScanPaymentRequest;
import com.foodify.server.modules.payments.points.repository.PointsPaymentRepository;
import com.foodify.server.modules.restaurants.domain.Restaurant;
import com.foodify.server.modules.restaurants.repository.RestaurantRepository;
import com.foodify.server.modules.rewards.repository.LoyaltyPointTransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PointsPaymentServiceTest {

    @Mock
    private PointsPaymentRepository pointsPaymentRepository;
    @Mock
    private RestaurantRepository restaurantRepository;
    @Mock
    private ClientRepository clientRepository;
    @Mock
    private LoyaltyPointTransactionRepository loyaltyTransactionRepository;
    @Mock
    private QrCodeService qrCodeService;
    @Mock
    private WebSocketService webSocketService;

    private PointsPaymentService pointsPaymentService;

    @BeforeEach
    void setUp() {
        pointsPaymentService = new PointsPaymentService(
                pointsPaymentRepository,
                restaurantRepository,
                clientRepository,
                loyaltyTransactionRepository,
                qrCodeService,
                webSocketService
        );
    }

    @Test
    void createPayment_shouldCreatePaymentWithCorrectPointsConversion() throws Exception {
        // Given
        Long restaurantId = 1L;
        BigDecimal amountTnd = new BigDecimal("100.00");
        CreatePointsPaymentRequest request = new CreatePointsPaymentRequest(amountTnd);

        Restaurant restaurant = new Restaurant();
        restaurant.setId(restaurantId);
        restaurant.setName("Test Restaurant");

        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.of(restaurant));
        when(qrCodeService.generateQrCodeImage(anyString())).thenReturn("base64QRCode");
        when(pointsPaymentRepository.save(any(PointsPayment.class))).thenAnswer(invocation -> {
            PointsPayment payment = invocation.getArgument(0);
            payment.setId(123L);
            return payment;
        });

        // When
        PointsPaymentResponse response = pointsPaymentService.createPayment(restaurantId, request);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getAmountTnd()).isEqualByComparingTo(new BigDecimal("100.00"));
        assertThat(response.getPointsAmount()).isEqualByComparingTo(new BigDecimal("10000.00")); // 100 TND * 100 = 10000 points
        assertThat(response.getStatus()).isEqualTo(PointsPaymentStatus.PENDING);
        assertThat(response.getQrCodeImage()).isEqualTo("base64QRCode");
        assertThat(response.getPaymentToken()).isNotNull();

        ArgumentCaptor<PointsPayment> paymentCaptor = ArgumentCaptor.forClass(PointsPayment.class);
        verify(pointsPaymentRepository).save(paymentCaptor.capture());
        PointsPayment savedPayment = paymentCaptor.getValue();
        assertThat(savedPayment.getPointsAmount()).isEqualByComparingTo(new BigDecimal("10000.00"));
    }

    @Test
    void createPayment_shouldThrowExceptionWhenRestaurantNotFound() {
        // Given
        Long restaurantId = 999L;
        CreatePointsPaymentRequest request = new CreatePointsPaymentRequest(new BigDecimal("100.00"));

        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> pointsPaymentService.createPayment(restaurantId, request))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Restaurant not found");

        verify(pointsPaymentRepository, never()).save(any());
    }

    @Test
    void createPayment_shouldThrowExceptionWhenAmountIsZero() {
        // Given
        Long restaurantId = 1L;
        CreatePointsPaymentRequest request = new CreatePointsPaymentRequest(BigDecimal.ZERO);

        Restaurant restaurant = new Restaurant();
        restaurant.setId(restaurantId);

        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.of(restaurant));

        // When & Then
        assertThatThrownBy(() -> pointsPaymentService.createPayment(restaurantId, request))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Amount must be greater than zero");
    }

    @Test
    void scanAndPay_shouldSuccessfullyTransferPoints() {
        // Given
        Long clientId = 1L;
        String paymentToken = "test-token-123";
        ScanPaymentRequest request = new ScanPaymentRequest(paymentToken);

        Client client = new Client();
        client.setId(clientId);
        client.setName("Test Client");
        client.setLoyaltyPointsBalance(new BigDecimal("10000.00"));

        Restaurant restaurant = new Restaurant();
        restaurant.setId(2L);
        restaurant.setName("Test Restaurant");
        restaurant.setPointsBalance(new BigDecimal("5000.00"));

        PointsPayment payment = new PointsPayment();
        payment.setId(1L);
        payment.setRestaurant(restaurant);
        payment.setAmountTnd(new BigDecimal("50.00"));
        payment.setPointsAmount(new BigDecimal("5000.00")); // 50 TND * 100 = 5000 points
        payment.setPaymentToken(paymentToken);
        payment.setStatus(PointsPaymentStatus.PENDING);
        payment.setExpiresAt(LocalDateTime.now().plusMinutes(30));

        when(clientRepository.findById(clientId)).thenReturn(Optional.of(client));
        when(pointsPaymentRepository.findByPaymentToken(paymentToken)).thenReturn(Optional.of(payment));
        when(pointsPaymentRepository.save(any(PointsPayment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        PointsPaymentResponse response = pointsPaymentService.scanAndPay(clientId, request);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getStatus()).isEqualTo(PointsPaymentStatus.COMPLETED);
        assertThat(response.getClientId()).isEqualTo(clientId);
        assertThat(client.getLoyaltyPointsBalance()).isEqualByComparingTo(new BigDecimal("5000.00")); // 10000.00 - 5000.00
        assertThat(restaurant.getPointsBalance()).isEqualByComparingTo(new BigDecimal("10000.00")); // 5000.00 + 5000.00

        verify(loyaltyTransactionRepository).save(any());
        verify(pointsPaymentRepository).save(any(PointsPayment.class));
        verify(webSocketService).notifyRestaurantPointsPayment(eq(2L), any(PointsPaymentResponse.class));
    }

    @Test
    void scanAndPay_shouldThrowExceptionWhenClientHasInsufficientPoints() {
        // Given
        Long clientId = 1L;
        String paymentToken = "test-token-123";
        ScanPaymentRequest request = new ScanPaymentRequest(paymentToken);

        Client client = new Client();
        client.setId(clientId);
        client.setName("Test Client");
        client.setLoyaltyPointsBalance(new BigDecimal("2500.00")); // Not enough points

        Restaurant restaurant = new Restaurant();
        restaurant.setId(2L);
        restaurant.setName("Test Restaurant");

        PointsPayment payment = new PointsPayment();
        payment.setId(1L);
        payment.setRestaurant(restaurant);
        payment.setAmountTnd(new BigDecimal("100.00"));
        payment.setPointsAmount(new BigDecimal("10000.00")); // Requires 10000 points
        payment.setPaymentToken(paymentToken);
        payment.setStatus(PointsPaymentStatus.PENDING);
        payment.setExpiresAt(LocalDateTime.now().plusMinutes(30));

        when(clientRepository.findById(clientId)).thenReturn(Optional.of(client));
        when(pointsPaymentRepository.findByPaymentToken(paymentToken)).thenReturn(Optional.of(payment));

        // When & Then
        assertThatThrownBy(() -> pointsPaymentService.scanAndPay(clientId, request))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Insufficient points");

        verify(pointsPaymentRepository, never()).save(any());
        verify(loyaltyTransactionRepository, never()).save(any());
    }

    @Test
    void scanAndPay_shouldThrowExceptionWhenPaymentIsExpired() {
        // Given
        Long clientId = 1L;
        String paymentToken = "test-token-123";
        ScanPaymentRequest request = new ScanPaymentRequest(paymentToken);

        Client client = new Client();
        client.setId(clientId);
        client.setLoyaltyPointsBalance(new BigDecimal("10000.00"));

        Restaurant restaurant = new Restaurant();
        restaurant.setId(2L);

        PointsPayment payment = new PointsPayment();
        payment.setId(1L);
        payment.setRestaurant(restaurant);
        payment.setAmountTnd(new BigDecimal("50.00"));
        payment.setPointsAmount(new BigDecimal("5000.00"));
        payment.setPaymentToken(paymentToken);
        payment.setStatus(PointsPaymentStatus.PENDING);
        payment.setExpiresAt(LocalDateTime.now().minusMinutes(5)); // Expired 5 minutes ago

        when(clientRepository.findById(clientId)).thenReturn(Optional.of(client));
        when(pointsPaymentRepository.findByPaymentToken(paymentToken)).thenReturn(Optional.of(payment));
        when(pointsPaymentRepository.save(any(PointsPayment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When & Then
        assertThatThrownBy(() -> pointsPaymentService.scanAndPay(clientId, request))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Payment has expired");

        verify(pointsPaymentRepository).save(payment);
        assertThat(payment.getStatus()).isEqualTo(PointsPaymentStatus.EXPIRED);
    }

    @Test
    void scanAndPay_shouldThrowExceptionWhenPaymentAlreadyCompleted() {
        // Given
        Long clientId = 1L;
        String paymentToken = "test-token-123";
        ScanPaymentRequest request = new ScanPaymentRequest(paymentToken);

        Client client = new Client();
        client.setId(clientId);

        Restaurant restaurant = new Restaurant();
        restaurant.setId(2L);

        PointsPayment payment = new PointsPayment();
        payment.setId(1L);
        payment.setRestaurant(restaurant);
        payment.setStatus(PointsPaymentStatus.COMPLETED); // Already completed

        when(clientRepository.findById(clientId)).thenReturn(Optional.of(client));
        when(pointsPaymentRepository.findByPaymentToken(paymentToken)).thenReturn(Optional.of(payment));

        // When & Then
        assertThatThrownBy(() -> pointsPaymentService.scanAndPay(clientId, request))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("already been completed");
    }

    @Test
    void scanAndPay_shouldThrowExceptionWhenPaymentTokenNotFound() {
        // Given
        Long clientId = 1L;
        String paymentToken = "invalid-token";
        ScanPaymentRequest request = new ScanPaymentRequest(paymentToken);

        Client client = new Client();
        client.setId(clientId);

        when(clientRepository.findById(clientId)).thenReturn(Optional.of(client));
        when(pointsPaymentRepository.findByPaymentToken(paymentToken)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> pointsPaymentService.scanAndPay(clientId, request))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Payment not found");
    }

    @Test
    void cancelPayment_shouldCancelPendingPayment() {
        // Given
        Long restaurantId = 1L;
        Long paymentId = 123L;

        Restaurant restaurant = new Restaurant();
        restaurant.setId(restaurantId);

        PointsPayment payment = new PointsPayment();
        payment.setId(paymentId);
        payment.setRestaurant(restaurant);
        payment.setStatus(PointsPaymentStatus.PENDING);

        when(pointsPaymentRepository.findById(paymentId)).thenReturn(Optional.of(payment));
        when(pointsPaymentRepository.save(any(PointsPayment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        pointsPaymentService.cancelPayment(restaurantId, paymentId);

        // Then
        verify(pointsPaymentRepository).save(payment);
        assertThat(payment.getStatus()).isEqualTo(PointsPaymentStatus.CANCELLED);
    }

    @Test
    void cancelPayment_shouldThrowExceptionWhenRestaurantDoesNotOwnPayment() {
        // Given
        Long restaurantId = 1L;
        Long paymentId = 123L;

        Restaurant restaurant = new Restaurant();
        restaurant.setId(999L); // Different restaurant

        PointsPayment payment = new PointsPayment();
        payment.setId(paymentId);
        payment.setRestaurant(restaurant);
        payment.setStatus(PointsPaymentStatus.PENDING);

        when(pointsPaymentRepository.findById(paymentId)).thenReturn(Optional.of(payment));

        // When & Then
        assertThatThrownBy(() -> pointsPaymentService.cancelPayment(restaurantId, paymentId))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("not authorized");
    }

    @Test
    void cancelPayment_shouldThrowExceptionWhenPaymentIsNotPending() {
        // Given
        Long restaurantId = 1L;
        Long paymentId = 123L;

        Restaurant restaurant = new Restaurant();
        restaurant.setId(restaurantId);

        PointsPayment payment = new PointsPayment();
        payment.setId(paymentId);
        payment.setRestaurant(restaurant);
        payment.setStatus(PointsPaymentStatus.COMPLETED);

        when(pointsPaymentRepository.findById(paymentId)).thenReturn(Optional.of(payment));

        // When & Then
        assertThatThrownBy(() -> pointsPaymentService.cancelPayment(restaurantId, paymentId))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("pending payments can be cancelled");
    }
}
