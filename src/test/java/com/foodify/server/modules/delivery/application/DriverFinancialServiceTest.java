package com.foodify.server.modules.delivery.application;

import com.foodify.server.modules.delivery.domain.Delivery;
import com.foodify.server.modules.delivery.domain.DriverDepositStatus;
import com.foodify.server.modules.delivery.repository.DriverDepositRepository;
import com.foodify.server.modules.identity.domain.Driver;
import com.foodify.server.modules.identity.repository.AdminRepository;
import com.foodify.server.modules.identity.repository.DriverRepository;
import com.foodify.server.modules.orders.domain.Order;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DriverFinancialServiceTest {

    @Mock
    private DriverRepository driverRepository;

    @Mock
    private DriverDepositRepository driverDepositRepository;

    @Mock
    private AdminRepository adminRepository;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    private DriverFinancialService driverFinancialService;

    @BeforeEach
    void setUp() {
        driverFinancialService = new DriverFinancialService(
                driverRepository,
                driverDepositRepository,
                adminRepository,
                eventPublisher
        );
    }

    @Test
    void testDailyFeeIs5DT() {
        assertEquals(new BigDecimal("5.00").setScale(2, RoundingMode.HALF_UP), 
                     DriverFinancialService.DAILY_FEE);
    }

    @Test
    void testDepositThresholdIs250DT() {
        assertEquals(new BigDecimal("250.00").setScale(2, RoundingMode.HALF_UP), 
                     DriverFinancialService.DEPOSIT_THRESHOLD);
    }

    @Test
    void testDepositDeadlineIs24Hours() {
        assertEquals(24, DriverFinancialService.DEPOSIT_DEADLINE_HOURS);
    }

    @Test
    void testRecordDeliveryCalculatesDriverEarningsCorrectly() {
        // Given: Order with deliveryFee = 10dt, tip = 5dt, and items that total 100dt
        Driver driver = new Driver();
        driver.setId(1L);
        driver.setCashOnHand(BigDecimal.ZERO);
        driver.setUnpaidEarnings(BigDecimal.ZERO);

        Delivery delivery = new Delivery();
        delivery.setDriver(driver);

        Order order = new Order();
        order.setDelivery(delivery);
        // Since OrderPricingCalculator needs items, we'll use the stored itemsTotal
        order.setItemsTotal(new BigDecimal("100.00"));
        order.setDeliveryFee(new BigDecimal("10.00"));
        order.setTipAmount(new BigDecimal("5.00"));
        order.setTotal(new BigDecimal("110.00"));
        order.setPaymentMethod("card");
        // Set empty items list to avoid null pointer
        order.setItems(new java.util.ArrayList<>());

        when(driverRepository.findById(1L)).thenReturn(Optional.of(driver));
        when(driverRepository.save(any(Driver.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        driverFinancialService.recordDelivery(order);

        // Then: Driver earnings = 0 (from empty items) * 12% + 10 + 5 = 0 + 10 + 5 = 15dt
        // Note: OrderPricingCalculator.calculateTotal returns 0 for empty items
        assertEquals(new BigDecimal("15.00").setScale(2, RoundingMode.HALF_UP), 
                     driver.getUnpaidEarnings());
    }

    @Test
    void testRecordDeliveryUpdatesCashOnHandForCashOrders() {
        // Given: Cash order with total = 110dt
        Driver driver = new Driver();
        driver.setId(1L);
        driver.setCashOnHand(new BigDecimal("50.00"));
        driver.setUnpaidEarnings(BigDecimal.ZERO);

        Delivery delivery = new Delivery();
        delivery.setDriver(driver);

        Order order = new Order();
        order.setDelivery(delivery);
        order.setItemsTotal(new BigDecimal("100.00"));
        order.setDeliveryFee(new BigDecimal("10.00"));
        order.setTotal(new BigDecimal("110.00"));
        order.setPaymentMethod("cash");
        order.setItems(new java.util.ArrayList<>());

        when(driverRepository.findById(1L)).thenReturn(Optional.of(driver));
        when(driverRepository.save(any(Driver.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        driverFinancialService.recordDelivery(order);

        // Then: Cash on hand = 50 + 110 = 160dt
        assertEquals(new BigDecimal("160.00").setScale(2, RoundingMode.HALF_UP), 
                     driver.getCashOnHand());
    }

    @Test
    void testIsDepositRequiredWhenCashOnHandAboveThreshold() {
        // Given
        Driver driver = new Driver();
        driver.setCashOnHand(new BigDecimal("250.00"));

        // When
        boolean result = driverFinancialService.isDepositRequired(driver);

        // Then
        assertTrue(result);
    }

    @Test
    void testIsDepositRequiredWhenCashOnHandBelowThreshold() {
        // Given
        Driver driver = new Driver();
        driver.setCashOnHand(new BigDecimal("249.99"));

        // When
        boolean result = driverFinancialService.isDepositRequired(driver);

        // Then
        assertFalse(result);
    }

    @Test
    void testHasDepositDeadlinePassedWhenDeadlineExceeded() {
        // Given: Warning sent 25 hours ago
        Driver driver = new Driver();
        driver.setDepositWarningSentAt(LocalDateTime.now().minusHours(25));

        // When
        boolean result = driverFinancialService.hasDepositDeadlinePassed(driver);

        // Then
        assertTrue(result);
    }

    @Test
    void testHasDepositDeadlinePassedWhenDeadlineNotExceeded() {
        // Given: Warning sent 23 hours ago
        Driver driver = new Driver();
        driver.setDepositWarningSentAt(LocalDateTime.now().minusHours(23));

        // When
        boolean result = driverFinancialService.hasDepositDeadlinePassed(driver);

        // Then
        assertFalse(result);
    }

    @Test
    void testHasDepositDeadlinePassedWhenNoWarningSent() {
        // Given: No warning sent
        Driver driver = new Driver();
        driver.setDepositWarningSentAt(null);

        // When
        boolean result = driverFinancialService.hasDepositDeadlinePassed(driver);

        // Then
        assertFalse(result);
    }

    @Test
    void testAssertCanWorkThrowsExceptionWhenDeadlinePassed() {
        // Given
        Driver driver = new Driver();
        driver.setCashOnHand(new BigDecimal("250.00"));
        driver.setDepositWarningSentAt(LocalDateTime.now().minusHours(25));

        // When & Then
        assertThrows(IllegalStateException.class, () -> {
            driverFinancialService.assertCanWork(driver);
        });
    }

    @Test
    void testAssertCanWorkDoesNotThrowWhenDeadlineNotPassed() {
        // Given
        Driver driver = new Driver();
        driver.setCashOnHand(new BigDecimal("249.99"));

        // When & Then
        assertDoesNotThrow(() -> {
            driverFinancialService.assertCanWork(driver);
        });
    }

    @Test
    void testCheckAndSendDepositWarningPublishesEventWhenThresholdReached() {
        // Given
        Driver driver = new Driver();
        driver.setId(1L);
        driver.setCashOnHand(new BigDecimal("250.00"));
        driver.setDepositWarningSentAt(null);

        when(driverRepository.save(any(Driver.class))).thenReturn(driver);

        // When
        driverFinancialService.checkAndSendDepositWarning(driver);

        // Then
        verify(driverRepository).save(any(Driver.class));
        verify(eventPublisher).publishEvent(any(DriverDepositWarningEvent.class));
    }

    @Test
    void testCheckAndSendDepositWarningDoesNotPublishEventWhenAlreadyWarned() {
        // Given
        Driver driver = new Driver();
        driver.setId(1L);
        driver.setCashOnHand(new BigDecimal("250.00"));
        driver.setDepositWarningSentAt(LocalDateTime.now().minusHours(1));

        // When
        driverFinancialService.checkAndSendDepositWarning(driver);

        // Then
        verify(driverRepository, never()).save(any(Driver.class));
        verify(eventPublisher, never()).publishEvent(any(DriverDepositWarningEvent.class));
    }

    @Test
    void testCheckAndSendDepositWarningDoesNotPublishEventWhenBelowThreshold() {
        // Given
        Driver driver = new Driver();
        driver.setId(1L);
        driver.setCashOnHand(new BigDecimal("249.99"));
        driver.setDepositWarningSentAt(null);

        // When
        driverFinancialService.checkAndSendDepositWarning(driver);

        // Then
        verify(driverRepository, never()).save(any(Driver.class));
        verify(eventPublisher, never()).publishEvent(any(DriverDepositWarningEvent.class));
    }
}
