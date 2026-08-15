package com.booknest.deliveryservice.controller;

import com.booknest.deliveryservice.model.Delivery;
import com.booknest.deliveryservice.service.DeliveryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeliveryControllerTest {

    @Mock
    private DeliveryService service;

    @InjectMocks
    private DeliveryController controller;

    private Delivery delivery;

    @BeforeEach
    void setUp() {
        delivery = new Delivery();
        delivery.setOrderId("order-123");
        delivery.setRiderId("RIDER-456");
        delivery.setStatus("ASSIGNED");
        delivery.setCurrentLat(6.9271);
        delivery.setCurrentLng(79.8612);
        delivery.setEta("30 mins");
    }

    @Test
    void testCreateDelivery() {
        when(service.create(any(Delivery.class))).thenReturn(delivery);

        Delivery result = controller.create(delivery).getBody();

        assertNotNull(result);
        assertEquals("order-123", result.getOrderId());
        assertEquals("RIDER-456", result.getRiderId());
        assertEquals("ASSIGNED", result.getStatus());
        verify(service, times(1)).create(any(Delivery.class));
    }

    @Test
    void testGetAllDeliveries() {
        Delivery delivery2 = new Delivery();
        delivery2.setOrderId("order-789");
        delivery2.setRiderId("RIDER-999");
        delivery2.setStatus("PICKED_UP");

        List<Delivery> deliveries = Arrays.asList(delivery, delivery2);
        when(service.getAll()).thenReturn(deliveries);

        List<Delivery> result = controller.getAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("order-123", result.get(0).getOrderId());
        assertEquals("order-789", result.get(1).getOrderId());
        verify(service, times(1)).getAll();
    }

    @Test
    void testGetDeliveryByOrderId() {
        when(service.getByOrderId("order-123")).thenReturn(delivery);

        Delivery result = controller.getByOrderId("order-123");

        assertNotNull(result);
        assertEquals("order-123", result.getOrderId());
        assertEquals("RIDER-456", result.getRiderId());
        assertEquals("ASSIGNED", result.getStatus());
        verify(service, times(1)).getByOrderId("order-123");
    }

    @Test
    void testUpdateDelivery() {
        Delivery updates = new Delivery();
        updates.setStatus("ON_THE_WAY");
        updates.setCurrentLat(6.9300);
        updates.setCurrentLng(79.8650);
        updates.setEta("20 mins");

        Delivery updated = new Delivery();
        updated.setOrderId("order-123");
        updated.setRiderId("RIDER-456");
        updated.setStatus("ON_THE_WAY");
        updated.setCurrentLat(6.9300);
        updated.setCurrentLng(79.8650);
        updated.setEta("20 mins");

        when(service.update(eq("order-123"), any(Delivery.class))).thenReturn(updated);

        Delivery result = controller.update("order-123", updates);

        assertNotNull(result);
        assertEquals("ON_THE_WAY", result.getStatus());
        assertEquals("20 mins", result.getEta());
        verify(service, times(1)).update(eq("order-123"), any(Delivery.class));
    }

    @Test
    void testCancelDelivery() {
        controller.cancel("order-123");

        verify(service, times(1)).cancel("order-123");
    }
}

