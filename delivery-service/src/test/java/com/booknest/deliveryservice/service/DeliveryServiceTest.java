package com.booknest.deliveryservice.service;

import com.booknest.deliveryservice.model.Delivery;
import com.booknest.deliveryservice.repository.DeliveryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeliveryServiceTest {

    @Mock
    private DeliveryRepository repository;

    @InjectMocks
    private DeliveryService service;

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
    void testCreateForOrderAssignsRider() {
        // Arrange
        ArgumentCaptor<Delivery> captor = ArgumentCaptor.forClass(Delivery.class);
        when(repository.save(any(Delivery.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Delivery result = service.createForOrder("order-123");

        // Assert
        assertNotNull(result);
        assertEquals("order-123", result.getOrderId());
        assertTrue(result.getRiderId().startsWith("RIDER-"));
        assertEquals("ASSIGNED", result.getStatus());
        assertEquals(6.9271, result.getCurrentLat());
        assertEquals(79.8612, result.getCurrentLng());
        assertEquals("30 mins", result.getEta());

        verify(repository).save(captor.capture());
    }

    @Test
    void testCreateForOrderGeneratesRandomRiderId() {
        // Arrange
        when(repository.save(any(Delivery.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Delivery delivery1 = service.createForOrder("order-1");
        Delivery delivery2 = service.createForOrder("order-2");

        // Assert - While not guaranteed to be different, statistically should be
        assertNotNull(delivery1.getRiderId());
        assertNotNull(delivery2.getRiderId());
        assertTrue(delivery1.getRiderId().startsWith("RIDER-"));
        assertTrue(delivery2.getRiderId().startsWith("RIDER-"));
    }

    @Test
    void testCreateForOrderSetsDefaultCoordinates() {
        // Arrange
        when(repository.save(any(Delivery.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Delivery result = service.createForOrder("order-test");

        // Assert
        assertEquals(6.9271, result.getCurrentLat(), 0.0001);
        assertEquals(79.8612, result.getCurrentLng(), 0.0001);
    }

    @Test
    void testCreate() {
        // Arrange
        when(repository.save(any(Delivery.class))).thenReturn(delivery);

        // Act
        Delivery result = service.create(delivery);

        // Assert
        assertNotNull(result);
        assertEquals("order-123", result.getOrderId());
        assertEquals("RIDER-456", result.getRiderId());
        verify(repository, times(1)).save(any(Delivery.class));
    }

    @Test
    void testGetAll() {
        // Arrange
        Delivery delivery2 = new Delivery();
        delivery2.setOrderId("order-789");
        delivery2.setStatus("ON_THE_WAY");

        List<Delivery> deliveries = Arrays.asList(delivery, delivery2);
        when(repository.findAll()).thenReturn(deliveries);

        // Act
        List<Delivery> result = service.getAll();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("order-123", result.get(0).getOrderId());
        assertEquals("order-789", result.get(1).getOrderId());
        verify(repository, times(1)).findAll();
    }

    @Test
    void testGetAllReturnsEmptyList() {
        // Arrange
        when(repository.findAll()).thenReturn(Arrays.asList());

        // Act
        List<Delivery> result = service.getAll();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetByOrderId() {
        // Arrange
        when(repository.findByOrderId("order-123")).thenReturn(Optional.of(delivery));

        // Act
        Delivery result = service.getByOrderId("order-123");

        // Assert
        assertNotNull(result);
        assertEquals("order-123", result.getOrderId());
        assertEquals("RIDER-456", result.getRiderId());
        verify(repository, times(1)).findByOrderId("order-123");
    }

    @Test
    void testGetByOrderIdThrowsExceptionWhenNotFound() {
        // Arrange
        when(repository.findByOrderId("nonexistent")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> service.getByOrderId("nonexistent"));
        verify(repository, times(1)).findByOrderId("nonexistent");
    }

    @Test
    void testGetByOrderIdExceptionContainsOrderId() {
        // Arrange
        String orderId = "order-missing";
        when(repository.findByOrderId(orderId)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, 
            () -> service.getByOrderId(orderId));
        assertTrue(exception.getMessage().contains("No delivery found"));
        assertTrue(exception.getMessage().contains(orderId));
    }

    @Test
    void testUpdateStatus() {
        // Arrange
        Delivery existing = new Delivery();
        existing.setOrderId("order-123");
        existing.setStatus("ASSIGNED");

        Delivery updates = new Delivery();
        updates.setStatus("ON_THE_WAY");

        when(repository.findByOrderId("order-123")).thenReturn(Optional.of(existing));
        when(repository.save(any(Delivery.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Delivery result = service.update("order-123", updates);

        // Assert
        assertEquals("ON_THE_WAY", result.getStatus());
        verify(repository).findByOrderId("order-123");
        verify(repository).save(any(Delivery.class));
    }

    @Test
    void testUpdateCoordinates() {
        // Arrange
        Delivery existing = new Delivery();
        existing.setOrderId("order-123");
        existing.setCurrentLat(6.9271);
        existing.setCurrentLng(79.8612);

        Delivery updates = new Delivery();
        updates.setCurrentLat(6.9300);
        updates.setCurrentLng(79.8650);

        when(repository.findByOrderId("order-123")).thenReturn(Optional.of(existing));
        when(repository.save(any(Delivery.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Delivery result = service.update("order-123", updates);

        // Assert
        assertEquals(6.9300, result.getCurrentLat());
        assertEquals(79.8650, result.getCurrentLng());
    }

    @Test
    void testUpdateEta() {
        // Arrange
        Delivery existing = new Delivery();
        existing.setOrderId("order-123");
        existing.setEta("30 mins");

        Delivery updates = new Delivery();
        updates.setEta("15 mins");

        when(repository.findByOrderId("order-123")).thenReturn(Optional.of(existing));
        when(repository.save(any(Delivery.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Delivery result = service.update("order-123", updates);

        // Assert
        assertEquals("15 mins", result.getEta());
    }

    @Test
    void testUpdateIgnoresNullValues() {
        // Arrange
        Delivery existing = new Delivery();
        existing.setOrderId("order-123");
        existing.setStatus("ASSIGNED");
        existing.setCurrentLat(6.9271);
        existing.setCurrentLng(79.8612);
        existing.setEta("30 mins");

        Delivery updates = new Delivery();
        updates.setStatus(null); // Null should be ignored
        updates.setEta(null);

        when(repository.findByOrderId("order-123")).thenReturn(Optional.of(existing));
        when(repository.save(any(Delivery.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Delivery result = service.update("order-123", updates);

        // Assert
        assertEquals("ASSIGNED", result.getStatus());
        assertEquals("30 mins", result.getEta());
    }

    @Test
    void testCancel() {
        // Arrange
        when(repository.findByOrderId("order-123")).thenReturn(Optional.of(delivery));

        // Act
        service.cancel("order-123");

        // Assert
        verify(repository).findByOrderId("order-123");
        verify(repository, times(1)).delete(delivery);
    }

    @Test
    void testCancelThrowsExceptionWhenNotFound() {
        // Arrange
        when(repository.findByOrderId("nonexistent")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> service.cancel("nonexistent"));
    }
}
