package com.foodify.server.modules.payments.konnect;

import com.foodify.server.modules.identity.domain.Client;
import com.foodify.server.modules.notifications.websocket.WebSocketService;
import com.foodify.server.modules.orders.domain.Order;
import com.foodify.server.modules.orders.repository.OrderRepository;
import com.foodify.server.modules.payments.konnect.dto.KonnectInitiatePaymentRequest;
import com.foodify.server.modules.payments.konnect.dto.KonnectInitiatePaymentResponse;
import com.foodify.server.modules.payments.konnect.dto.KonnectPaymentDetailsResponse;
import com.foodify.server.modules.payments.konnect.exception.KonnectApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeParseException;
import java.util.Locale;
import java.util.Optional;

@Service
public class KonnectPaymentService {

    private static final Logger log = LoggerFactory.getLogger(KonnectPaymentService.class);
    private static final String DEFAULT_STATUS = "pending";

    private final KonnectProperties properties;
    private final KonnectApiClient apiClient;
    private final OrderRepository orderRepository;
    private final WebSocketService webSocketService;

    public KonnectPaymentService(KonnectProperties properties,
                                 KonnectApiClient apiClient,
                                 OrderRepository orderRepository,
                                 WebSocketService webSocketService) {
        this.properties = properties;
        this.apiClient = apiClient;
        this.orderRepository = orderRepository;
        this.webSocketService = webSocketService;
    }

    public Optional<KonnectPaymentSummary> initiatePayment(Order order, Client client) {
        if (order == null || !properties.isEnabled()) {
            return Optional.empty();
        }

        KonnectProperties.EnvironmentConfiguration environment = properties.getActiveEnvironment();
        if (environment == null || !environment.isReady()) {
            log.warn("Konnect environment [{}] is not fully configured. Skipping payment initiation.",
                    properties.getEnvironment());
            return Optional.empty();
        }

        KonnectProperties.PaymentOptions paymentOptions = properties.getPayment();
        long amount = toMinorUnits(Optional.ofNullable(order.getTotal()).orElse(BigDecimal.ZERO), paymentOptions);

        String[] nameParts = splitName(client != null ? client.getName() : null);

        KonnectInitiatePaymentRequest request = new KonnectInitiatePaymentRequest(
                environment.getReceiverWalletId(),
                paymentOptions.getToken(),
                amount,
                paymentOptions.getType(),
                buildDescription(order, paymentOptions),
                paymentOptions.getAcceptedMethods(),
                paymentOptions.getLifespanMinutes(),
                paymentOptions.getCheckoutForm(),
                paymentOptions.getAddPaymentFeesToAmount(),
                nameParts[0],
                nameParts[1],
                normalizePhone(client != null ? client.getPhoneNumber() : null),
                client != null ? client.getEmail() : null,
                order.getId() != null ? order.getId().toString() : null,
                environment.getWebhook(),
                paymentOptions.getTheme()
        );

        try {
            KonnectInitiatePaymentResponse response = apiClient.initiatePayment(
                    environment.getBaseUrl(),
                    environment.getApiKey(),
                    request
            );
            return Optional.of(new KonnectPaymentSummary(
                    response.payUrl(),
                    response.paymentRef(),
                    DEFAULT_STATUS,
                    environment.getName(),
                    computeExpiration(paymentOptions)
            ));
        } catch (KonnectApiException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY,
                    "Unable to initiate payment with Konnect", ex);
        }
    }

    @Transactional
    public boolean handleWebhook(String paymentReference) {
        if (!StringUtils.hasText(paymentReference)) {
            log.debug("Ignoring Konnect webhook without payment reference");
            return false;
        }
        if (!properties.isEnabled()) {
            log.debug("Konnect integration disabled, ignoring webhook for [{}]", paymentReference);
            return false;
        }

        KonnectProperties.EnvironmentConfiguration environment = properties.getActiveEnvironment();
        if (environment == null || !environment.isReady()) {
            log.warn("Konnect environment [{}] is not fully configured. Skipping webhook for [{}]",
                    properties.getEnvironment(), paymentReference);
            return false;
        }

        KonnectPaymentDetailsResponse response;
        try {
            response = apiClient.getPaymentDetails(
                    environment.getBaseUrl(),
                    environment.getApiKey(),
                    paymentReference
            );
        } catch (KonnectApiException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY,
                    "Unable to fetch payment from Konnect", ex);
        }

        KonnectPaymentDetailsResponse.Payment payment = response.payment();
        if (payment == null) {
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY,
                    "Konnect response is missing payment details");
        }

        Order order = resolveOrder(paymentReference, payment);
        if (order == null) {
            log.warn("Received Konnect webhook for unknown payment reference [{}]", paymentReference);
            return false;
        }

        applyWebhookUpdate(order, payment, environment.getName());
        Order saved = orderRepository.save(order);
        notifyParties(saved);
        log.info("Processed Konnect webhook for order {} with payment status {}",
                saved.getId(), saved.getPaymentStatus());
        return true;
    }

    private Order resolveOrder(String paymentReference, KonnectPaymentDetailsResponse.Payment payment) {
        Order order = null;
        if (payment != null && StringUtils.hasText(payment.orderId())) {
            try {
                long orderId = Long.parseLong(payment.orderId().trim());
                order = orderRepository.findById(orderId).orElse(null);
            } catch (NumberFormatException ex) {
                log.warn("Konnect payment order id [{}] is not a valid number", payment.orderId());
            }
        }

        if (order == null) {
            order = orderRepository.findByPaymentReference(paymentReference)
                    .orElse(null);
        }

        if (order != null && !StringUtils.hasText(order.getPaymentReference())) {
            order.setPaymentReference(paymentReference);
        }

        return order;
    }

    private void applyWebhookUpdate(Order order,
                                    KonnectPaymentDetailsResponse.Payment payment,
                                    String environmentName) {
        order.setPaymentStatus(normalizeStatus(payment.status()));
        if (StringUtils.hasText(payment.link())) {
            order.setPaymentUrl(payment.link());
        }
        if (StringUtils.hasText(environmentName)) {
            order.setPaymentEnvironment(environmentName);
        }
        LocalDateTime expiration = parseExpiration(payment.expirationDate());
        if (expiration != null) {
            order.setPaymentExpiresAt(expiration);
        }
    }

    private void notifyParties(Order order) {
        if (order.getClient() != null && order.getClient().getId() != null) {
            webSocketService.notifyClient(order.getClient().getId(), order);
        }
        if (order.getRestaurant() != null && order.getRestaurant().getId() != null) {
            webSocketService.notifyRestaurant(order.getRestaurant().getId(), order);
        }
    }

    private String normalizeStatus(String status) {
        if (!StringUtils.hasText(status)) {
            return null;
        }
        String normalized = status.trim().toLowerCase(Locale.ROOT);
        return switch (normalized) {
            case "completed", "paid", "success" -> "paid";
            case "pending", "processing", "initiated" -> "pending";
            case "failed", "canceled", "cancelled", "declined", "error" -> "failed";
            case "expired" -> "expired";
            default -> normalized;
        };
    }

    private LocalDateTime parseExpiration(String expiration) {
        if (!StringUtils.hasText(expiration)) {
            return null;
        }
        try {
            return OffsetDateTime.parse(expiration).toLocalDateTime();
        } catch (DateTimeParseException ignored) {
            try {
                return LocalDateTime.parse(expiration);
            } catch (DateTimeParseException innerIgnored) {
                try {
                    return LocalDate.parse(expiration).atTime(LocalTime.MAX);
                } catch (DateTimeParseException finalIgnored) {
                    log.warn("Unable to parse Konnect expiration [{}]", expiration);
                    return null;
                }
            }
        }
    }

    private long toMinorUnits(BigDecimal amount, KonnectProperties.PaymentOptions paymentOptions) {
        int scale = Optional.ofNullable(paymentOptions.getAmountScale()).orElse(1000);
        BigDecimal scaled = amount.multiply(BigDecimal.valueOf(scale));
        return scaled.setScale(0, RoundingMode.HALF_UP).longValue();
    }

    private String[] splitName(String fullName) {
        if (!StringUtils.hasText(fullName)) {
            return new String[] { null, null };
        }
        String trimmed = fullName.trim();
        int spaceIndex = trimmed.indexOf(' ');
        if (spaceIndex <= 0) {
            return new String[] { trimmed, null };
        }
        String first = trimmed.substring(0, spaceIndex).trim();
        String last = trimmed.substring(spaceIndex + 1).trim();
        if (!StringUtils.hasText(last)) {
            last = null;
        }
        return new String[] { first, last };
    }

    private String normalizePhone(String phoneNumber) {
        if (!StringUtils.hasText(phoneNumber)) {
            return null;
        }
        return phoneNumber.replaceAll("\\s+", "");
    }

    private String buildDescription(Order order, KonnectProperties.PaymentOptions paymentOptions) {
        String template = paymentOptions.getDescriptionTemplate();
        if (!StringUtils.hasText(template)) {
            return orderDescriptionFallback(order);
        }
        String description = template
                .replace("{orderId}", order.getId() != null ? order.getId().toString() : "")
                .replace("{restaurant}", Optional.ofNullable(order.getRestaurant())
                        .map(restaurant -> restaurant.getName())
                        .orElse(""))
                .trim();
        if (!StringUtils.hasText(description)) {
            return orderDescriptionFallback(order);
        }
        return description;
    }

    private String orderDescriptionFallback(Order order) {
        StringBuilder builder = new StringBuilder("Foodify order");
        if (order != null && order.getId() != null) {
            builder.append(" #").append(order.getId());
        }
        return builder.toString();
    }

    private LocalDateTime computeExpiration(KonnectProperties.PaymentOptions paymentOptions) {
        Integer lifespan = paymentOptions.getLifespanMinutes();
        if (lifespan == null || lifespan <= 0) {
            return null;
        }
        return LocalDateTime.now(ZoneId.systemDefault()).plusMinutes(lifespan);
    }

    public record KonnectPaymentSummary(
            String paymentUrl,
            String paymentReference,
            String status,
            String environment,
            LocalDateTime expiresAt
    ) {
    }
}
