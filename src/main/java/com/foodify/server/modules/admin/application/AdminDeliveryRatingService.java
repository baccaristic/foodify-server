package com.foodify.server.modules.admin.application;

import com.foodify.server.modules.delivery.domain.Delivery;
import com.foodify.server.modules.delivery.domain.DeliveryRating;
import com.foodify.server.modules.admin.dto.DeliveryRatingRequest;
import com.foodify.server.modules.admin.dto.DeliveryRatingResponse;
import com.foodify.server.modules.delivery.dto.DriverRatingSummaryDto;
import com.foodify.server.modules.delivery.repository.DeliveryRatingRepository;
import com.foodify.server.modules.orders.domain.Order;
import com.foodify.server.modules.orders.domain.OrderStatus;
import com.foodify.server.modules.orders.repository.OrderRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdminDeliveryRatingService {

    private static final int MAX_PAGE_SIZE = 100;

    private final DeliveryRatingRepository deliveryRatingRepository;
    private final OrderRepository orderRepository;

    @Transactional
    public DeliveryRatingResponse rateDelivery(Long clientId, Long orderId, DeliveryRatingRequest request) {
        Order order = resolveOrder(orderId);

        if (!order.getClient().getId().equals(clientId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You can only rate your own deliveries");
        }

        ensureOrderIsDeliverable(order);
        Delivery delivery = ensureDeliveryExists(order);

        return saveRating(delivery, request);
    }

    @Transactional
    public Optional<DeliveryRatingResponse> findRatingForOrder(Long clientId, Long orderId) {
        Order order = resolveOrder(orderId);
        if (!order.getClient().getId().equals(clientId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You can only view your own delivery ratings");
        }
        return deliveryRatingRepository.findByDelivery_Order_Id(orderId)
                .map(this::toResponse);
    }

    @Transactional
    public DeliveryRatingResponse upsertRatingForAdmin(Long orderId, DeliveryRatingRequest request) {
        Order order = resolveOrder(orderId);
        Delivery delivery = ensureDeliveryExists(order);
        return saveRating(delivery, request);
    }

    @Transactional
    public DriverRatingSummaryDto getDriverSummary(Long driverId) {
        if (driverId == null) {
            throw new IllegalArgumentException("Driver id is required");
        }
        return deliveryRatingRepository.findSummaryByDriverId(driverId)
                .orElse(DriverRatingSummaryDto.empty(driverId));
    }

    @Transactional
    public List<DeliveryRatingResponse> getRatingsForDriver(Long driverId, int limit) {
        if (driverId == null) {
            throw new IllegalArgumentException("Driver id is required");
        }
        int pageSize = Math.min(Math.max(limit, 1), MAX_PAGE_SIZE);
        Page<DeliveryRating> page = deliveryRatingRepository
                .findByDelivery_Driver_IdOrderByCreatedAtDesc(driverId, PageRequest.of(0, pageSize));
        return page.stream()
                .map(this::toResponse)
                .toList();
    }

    private DeliveryRatingResponse saveRating(Delivery delivery, DeliveryRatingRequest request) {
        if (request == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Rating details are required");
        }
        validateRatingValue("timing", request.timing());
        validateRatingValue("foodCondition", request.foodCondition());
        validateRatingValue("professionalism", request.professionalism());
        validateRatingValue("overall", request.overall());

        DeliveryRating rating = deliveryRatingRepository.findById(delivery.getId())
                .orElseGet(() -> {
                    DeliveryRating newRating = new DeliveryRating();
                    newRating.setDelivery(delivery);
                    return newRating;
                });

        rating.setTimingRating(request.timing());
        rating.setFoodConditionRating(request.foodCondition());
        rating.setProfessionalismRating(request.professionalism());
        rating.setOverallRating(request.overall());
        rating.setComments(StringUtils.hasText(request.comments()) ? request.comments().trim() : null);

        if (rating.getCreatedAt() == null) {
            rating.setCreatedAt(LocalDateTime.now());
        }
        rating.setUpdatedAt(LocalDateTime.now());

        DeliveryRating saved = deliveryRatingRepository.save(rating);
        return toResponse(saved);
    }

    private void ensureOrderIsDeliverable(Order order) {
        if (order.getStatus() != OrderStatus.DELIVERED) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Only delivered orders can be rated");
        }
    }

    private Delivery ensureDeliveryExists(Order order) {
        Delivery delivery = Optional.ofNullable(order.getDelivery())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.CONFLICT,
                        "Delivery information is not available for this order"));
        if (delivery.getDriver() == null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "No driver assigned to this delivery");
        }
        return delivery;
    }

    private void validateRatingValue(String field, Integer value) {
        if (value == null || value < 1 || value > 5) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Rating for " + field + " must be between 1 and 5");
        }
    }

    private Order resolveOrder(Long orderId) {
        if (orderId == null) {
            throw new IllegalArgumentException("Order id is required");
        }
        return orderRepository.findDetailedById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));
    }

    private DeliveryRatingResponse toResponse(DeliveryRating rating) {
        Delivery delivery = rating.getDelivery();
        Order order = delivery.getOrder();
        Long clientId = order.getClient() != null ? order.getClient().getId() : null;
        String clientName = order.getClient() != null ? order.getClient().getName() : null;
        Long driverId = delivery.getDriver() != null ? delivery.getDriver().getId() : null;

        return new DeliveryRatingResponse(
                order.getId(),
                delivery.getId(),
                driverId,
                clientId,
                clientName,
                rating.getTimingRating(),
                rating.getFoodConditionRating(),
                rating.getProfessionalismRating(),
                rating.getOverallRating(),
                rating.getComments(),
                rating.getCreatedAt(),
                rating.getUpdatedAt()
        );
    }
}
