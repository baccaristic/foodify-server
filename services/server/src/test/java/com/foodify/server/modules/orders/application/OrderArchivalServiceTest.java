package com.foodify.server.modules.orders.application;

import com.foodify.server.modules.orders.domain.Order;
import com.foodify.server.modules.orders.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class OrderArchivalServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderLifecycleService orderLifecycleService;

    @InjectMocks
    private OrderArchivalService orderArchivalService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(orderArchivalService, "archiveAfterHours", 72L);
        ReflectionTestUtils.setField(orderArchivalService, "archiveBatchSize", 1);
    }

    @Test
    void archivesOrdersInConfiguredBatches() {
        Order first = new Order();
        Order second = new Order();

        Slice<Order> firstSlice = new SliceImpl<>(List.of(first), PageRequest.of(0, 1), true);
        Slice<Order> secondSlice = new SliceImpl<>(List.of(second), PageRequest.of(0, 1), false);
        Slice<Order> emptySlice = new SliceImpl<>(List.of(), PageRequest.of(0, 1), false);

        when(orderRepository.findArchivableOrders(any(), any(), any()))
                .thenReturn(firstSlice, secondSlice, emptySlice);
        when(orderRepository.saveAll(any())).thenAnswer(invocation -> invocation.getArgument(0));

        orderArchivalService.archiveCompletedOrders();

        assertThat(first.getArchivedAt()).isNotNull();
        assertThat(second.getArchivedAt()).isNotNull();

        verify(orderRepository, times(3)).findArchivableOrders(any(), any(), any());

        @SuppressWarnings("unchecked")
        ArgumentCaptor<List<Order>> captor = ArgumentCaptor.forClass((Class) List.class);
        verify(orderRepository, times(2)).saveAll(captor.capture());
        assertThat(captor.getAllValues()).allSatisfy(batch -> assertThat(batch).hasSize(1));

        verify(orderLifecycleService, times(2))
                .recordArchive(any(Order.class), eq("system:archiver"), eq("Order archived automatically"));
    }

    @Test
    void skipsWorkWhenNoCandidates() {
        Slice<Order> emptySlice = new SliceImpl<>(List.of(), PageRequest.of(0, 1), false);
        when(orderRepository.findArchivableOrders(any(), any(), any())).thenReturn(emptySlice);

        orderArchivalService.archiveCompletedOrders();

        verify(orderRepository, times(1)).findArchivableOrders(any(), any(), any());
        verify(orderRepository, times(0)).saveAll(any());
        verify(orderLifecycleService, times(0)).recordArchive(any(), any(), any());
    }
}
