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
import com.foodify.server.modules.rewards.domain.LoyaltyPointTransaction;
import com.foodify.server.modules.rewards.domain.LoyaltyPointTransactionType;
import com.foodify.server.modules.rewards.repository.LoyaltyPointTransactionRepository;
import com.google.zxing.WriterException;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class PointsPaymentService {

    // Conversion rate: 1 Foodify point = 0.01 TND, so 1 TND = 100 Foodify points
    private static final BigDecimal TND_TO_POINTS_RATE = BigDecimal.valueOf(100);
    
    // Payment expiration time in minutes
    private static final int PAYMENT_EXPIRATION_MINUTES = 30;

    private final PointsPaymentRepository pointsPaymentRepository;
    private final RestaurantRepository restaurantRepository;
    private final ClientRepository clientRepository;
    private final LoyaltyPointTransactionRepository loyaltyTransactionRepository;
    private final QrCodeService qrCodeService;
    private final WebSocketService webSocketService;

    public PointsPaymentService(PointsPaymentRepository pointsPaymentRepository,
                                RestaurantRepository restaurantRepository,
                                ClientRepository clientRepository,
                                LoyaltyPointTransactionRepository loyaltyTransactionRepository,
                                QrCodeService qrCodeService,
                                WebSocketService webSocketService) {
        this.pointsPaymentRepository = pointsPaymentRepository;
        this.restaurantRepository = restaurantRepository;
        this.clientRepository = clientRepository;
        this.loyaltyTransactionRepository = loyaltyTransactionRepository;
        this.qrCodeService = qrCodeService;
        this.webSocketService = webSocketService;
    }

    /**
     * Creates a payment request for a restaurant
     * @param restaurantId The ID of the restaurant creating the payment
     * @param request The payment details (amount in TND)
     * @return The payment response with QR code
     */
    public PointsPaymentResponse createPayment(Long restaurantId, CreatePointsPaymentRequest request) {
        // Validate restaurant exists
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Restaurant not found"));

        // Validate amount
        if (request.getAmountTnd() == null || request.getAmountTnd().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Amount must be greater than zero");
        }

        // Calculate points amount (1 TND = 100 points)
        BigDecimal pointsAmount = request.getAmountTnd()
                .multiply(TND_TO_POINTS_RATE)
                .setScale(2, RoundingMode.HALF_UP);

        // Generate unique payment token
        String paymentToken = generatePaymentToken();

        // Create payment entity
        PointsPayment payment = new PointsPayment();
        payment.setRestaurant(restaurant);
        payment.setAmountTnd(request.getAmountTnd().setScale(2, RoundingMode.HALF_UP));
        payment.setPointsAmount(pointsAmount);
        payment.setPaymentToken(paymentToken);
        payment.setStatus(PointsPaymentStatus.PENDING);
        payment.setCreatedAt(LocalDateTime.now());
        payment.setExpiresAt(LocalDateTime.now().plusMinutes(PAYMENT_EXPIRATION_MINUTES));

        PointsPayment savedPayment = pointsPaymentRepository.save(payment);

        // Generate QR code
        String qrCodeImage;
        try {
            qrCodeImage = qrCodeService.generateQrCodeImage(paymentToken);
        } catch (WriterException | IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, 
                    "Failed to generate QR code", e);
        }

        return mapToResponse(savedPayment, qrCodeImage);
    }

    /**
     * Processes a payment when a client scans the QR code
     * @param clientId The ID of the client making the payment
     * @param request The scan request containing the payment token
     * @return The completed payment response
     */
    public PointsPaymentResponse scanAndPay(Long clientId, ScanPaymentRequest request) {
        // Validate client exists
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Client not found"));

        // Find payment by token
        PointsPayment payment = pointsPaymentRepository.findByPaymentToken(request.getPaymentToken())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, 
                        "Payment not found or invalid QR code"));

        // Validate payment status
        if (payment.getStatus() != PointsPaymentStatus.PENDING) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, 
                    "Payment has already been " + payment.getStatus().name().toLowerCase());
        }

        // Check if payment has expired
        if (LocalDateTime.now().isAfter(payment.getExpiresAt())) {
            payment.setStatus(PointsPaymentStatus.EXPIRED);
            pointsPaymentRepository.save(payment);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Payment has expired");
        }

        // Validate client has sufficient points
        BigDecimal clientBalance = Optional.ofNullable(client.getLoyaltyPointsBalance())
                .orElse(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP));

        if (clientBalance.compareTo(payment.getPointsAmount()) < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, 
                    String.format("Insufficient points. Required: %s, Available: %s", 
                            payment.getPointsAmount(), clientBalance));
        }

        // Process the payment
        // 1. Deduct points from client
        BigDecimal newClientBalance = clientBalance.subtract(payment.getPointsAmount())
                .setScale(2, RoundingMode.HALF_UP);
        client.setLoyaltyPointsBalance(newClientBalance);

        // 2. Add points to restaurant
        Restaurant restaurant = payment.getRestaurant();
        BigDecimal restaurantBalance = Optional.ofNullable(restaurant.getPointsBalance())
                .orElse(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP));
        BigDecimal newRestaurantBalance = restaurantBalance.add(payment.getPointsAmount())
                .setScale(2, RoundingMode.HALF_UP);
        restaurant.setPointsBalance(newRestaurantBalance);

        // 3. Update payment status
        payment.setClient(client);
        payment.setStatus(PointsPaymentStatus.COMPLETED);
        payment.setCompletedAt(LocalDateTime.now());
        pointsPaymentRepository.save(payment);

        // 4. Create loyalty transaction record for client
        LoyaltyPointTransaction clientTransaction = new LoyaltyPointTransaction();
        clientTransaction.setClient(client);
        clientTransaction.setType(LoyaltyPointTransactionType.REDEEMED);
        clientTransaction.setPoints(payment.getPointsAmount().negate());
        clientTransaction.setDescription(String.format("Payment to %s (%.2f TND)", 
                restaurant.getName(), payment.getAmountTnd()));
        loyaltyTransactionRepository.save(clientTransaction);

        // 5. Notify restaurant via WebSocket
        notifyRestaurantPaymentCompleted(restaurant.getId(), payment);

        return mapToResponse(payment, null);
    }

    /**
     * Gets the payment history for a restaurant
     * @param restaurantId The restaurant ID
     * @return List of payment responses
     */
    @Transactional(Transactional.TxType.SUPPORTS)
    public List<PointsPaymentResponse> getRestaurantPayments(Long restaurantId) {
        return pointsPaymentRepository.findByRestaurant_IdOrderByCreatedAtDesc(restaurantId)
                .stream()
                .map(payment -> {
                    try {
                        return mapToResponse(payment, qrCodeService.generateQrCodeImage(payment.getPaymentToken()));
                    } catch (WriterException e) {
                        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to generate QR code", e);
                    } catch (IOException e) {
                        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to generate QR code", e);
                    }
                })
                .collect(Collectors.toList());
    }

    /**
     * Gets the payment history for a client
     * @param clientId The client ID
     * @return List of payment responses
     */
    @Transactional(Transactional.TxType.SUPPORTS)
    public List<PointsPaymentResponse> getClientPayments(Long clientId) {
        return pointsPaymentRepository.findByClient_IdOrderByCreatedAtDesc(clientId)
                .stream()
                .map(payment -> mapToResponse(payment, null))
                .collect(Collectors.toList());
    }

    /**
     * Gets the details of a specific payment
     * @param paymentId The payment ID
     * @return The payment response
     */
    @Transactional(Transactional.TxType.SUPPORTS)
    public PointsPaymentResponse getPayment(Long paymentId) {
        PointsPayment payment = pointsPaymentRepository.findById(paymentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Payment not found"));
        return mapToResponse(payment, null);
    }

    /**
     * Cancels a pending payment
     * @param restaurantId The restaurant ID
     * @param paymentId The payment ID
     */
    public void cancelPayment(Long restaurantId, Long paymentId) {
        PointsPayment payment = pointsPaymentRepository.findById(paymentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Payment not found"));

        // Validate restaurant owns this payment
        if (!payment.getRestaurant().getId().equals(restaurantId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, 
                    "You are not authorized to cancel this payment");
        }

        // Validate payment is pending
        if (payment.getStatus() != PointsPaymentStatus.PENDING) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, 
                    "Only pending payments can be cancelled");
        }

        payment.setStatus(PointsPaymentStatus.CANCELLED);
        pointsPaymentRepository.save(payment);
    }

    /**
     * Generates a unique payment token
     */
    private String generatePaymentToken() {
        return UUID.randomUUID().toString();
    }

    /**
     * Notifies restaurant via WebSocket when a payment is completed
     */
    private void notifyRestaurantPaymentCompleted(Long restaurantId, PointsPayment payment) {
        try {
            PointsPaymentResponse notification = mapToResponse(payment, null);
            webSocketService.notifyRestaurantPointsPayment(restaurantId, notification);
        } catch (Exception e) {
            // Log the error but don't fail the payment transaction
            // The payment is already completed, notification failure shouldn't roll it back
            System.err.println("Failed to send WebSocket notification for payment " + payment.getId() + ": " + e.getMessage());
        }
    }

    /**
     * Maps a PointsPayment entity to a PointsPaymentResponse DTO
     */
    private PointsPaymentResponse mapToResponse(PointsPayment payment, String qrCodeImage) {
        PointsPaymentResponse response = new PointsPaymentResponse();
        response.setId(payment.getId());
        response.setRestaurantId(payment.getRestaurant().getId());
        response.setRestaurantName(payment.getRestaurant().getName());
        
        if (payment.getClient() != null) {
            response.setClientId(payment.getClient().getId());
            response.setClientName(payment.getClient().getName());
        }
        
        response.setAmountTnd(payment.getAmountTnd());
        response.setPointsAmount(payment.getPointsAmount());
        response.setPaymentToken(payment.getPaymentToken());
        response.setStatus(payment.getStatus());
        response.setCreatedAt(payment.getCreatedAt());
        response.setCompletedAt(payment.getCompletedAt());
        response.setExpiresAt(payment.getExpiresAt());
        response.setQrCodeImage(qrCodeImage);
        
        return response;
    }
}
