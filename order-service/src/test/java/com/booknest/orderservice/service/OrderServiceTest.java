package com.booknest.orderservice.service;

import com.booknest.orderservice.model.Order;
import com.booknest.orderservice.model.OrderItem;
import com.booknest.orderservice.model.OrderPlacedEvent;
import com.booknest.orderservice.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository repository;

    @Mock
    private RabbitTemplate rabbitTemplate;

    @InjectMocks
    private OrderService service;

    private Order order;
    private OrderItem item;

    @BeforeEach
    void setUp() {
        item = new OrderItem();
        item.setBookId("book-1");
        item.setTitle("The Great Gatsby");
        item.setQuantity(2);
        item.setUnitPrice(12.99);

        order = new Order();
        order.setId("order-123");
        order.setUserId("user-456");
        order.setItems(Arrays.asList(item));
        order.setDeliveryAddress("123 Main St");
    }

    @Test
    void testCheckoutCalculatesTotalAmount() {
        // Arrange
        Order checkoutOrder = new Order();
        checkoutOrder.setUserId("user-456");
        checkoutOrder.setItems(Arrays.asList(item));
        checkoutOrder.setDeliveryAddress("123 Main St");

        Order savedOrder = new Order();
        savedOrder.setId("order-123");
        savedOrder.setUserId("user-456");
        savedOrder.setItems(Arrays.asList(item));
        savedOrder.setTotalAmount(25.98);
        savedOrder.setStatus("PLACED");

        when(repository.save(any(Order.class))).thenReturn(savedOrder);

        // Act
        Order result = service.checkout(checkoutOrder);

        // Assert
        assertEquals(25.98, result.getTotalAmount(), 0.01);
        assertEquals("PLACED", result.getStatus());
        verify(repository).save(any(Order.class));
    }

    @Test
    void testCheckoutCalculatesTotalAmountMultipleItems() {
        // Arrange
        OrderItem item1 = new OrderItem();
        item1.setQuantity(2);
        item1.setUnitPrice(10.00);

        OrderItem item2 = new OrderItem();
        item2.setQuantity(3);
        item2.setUnitPrice(15.00);

        Order checkoutOrder = new Order();
        checkoutOrder.setUserId("user-456");
        checkoutOrder.setItems(Arrays.asList(item1, item2));

        Order savedOrder = new Order();
        savedOrder.setId("order-123");
        savedOrder.setUserId("user-456");
        savedOrder.setItems(Arrays.asList(item1, item2));
        savedOrder.setTotalAmount(65.00); // (2*10) + (3*15) = 65
        savedOrder.setStatus("PLACED");

        when(repository.save(any(Order.class))).thenReturn(savedOrder);

        // Act
        Order result = service.checkout(checkoutOrder);

        // Assert
        assertEquals(65.00, result.getTotalAmount(), 0.01);
    }

    @Test
    void testCheckoutPublishesEvent() {
        // Arrange
        Order checkoutOrder = new Order();
        checkoutOrder.setUserId("user-456");
        checkoutOrder.setItems(Arrays.asList(item));

        Order savedOrder = new Order();
        savedOrder.setId("order-123");
        savedOrder.setUserId("user-456");
        savedOrder.setItems(Arrays.asList(item));
        savedOrder.setTotalAmount(25.98);
        savedOrder.setStatus("PLACED");

        when(repository.save(any(Order.class))).thenReturn(savedOrder);

        // Act
        service.checkout(checkoutOrder);

        // Assert
        ArgumentCaptor<OrderPlacedEvent> eventCaptor = ArgumentCaptor.forClass(OrderPlacedEvent.class);
        verify(rabbitTemplate).convertAndSend(anyString(), eventCaptor.capture());

        OrderPlacedEvent event = eventCaptor.getValue();
        assertEquals("order-123", event.getOrderId());
        assertEquals("user-456", event.getUserId());
        assertEquals(25.98, event.getTotalAmount(), 0.01);
    }

    @Test
    void testCheckoutSetsStatus() {
        // Arrange
        Order checkoutOrder = new Order();
        checkoutOrder.setUserId("user-456");
        checkoutOrder.setItems(Arrays.asList(item));

        ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);
        Order savedOrder = new Order();
        savedOrder.setStatus("PLACED");

        when(repository.save(any(Order.class))).thenReturn(savedOrder);

        // Act
        service.checkout(checkoutOrder);

        // Assert
        verify(repository).save(orderCaptor.capture());
        assertEquals("PLACED", orderCaptor.getValue().getStatus());
    }

    @Test
    void testGetAll() {
        // Arrange
        Order order2 = new Order();
        order2.setId("order-789");

        List<Order> orders = Arrays.asList(order, order2);
        when(repository.findAll()).thenReturn(orders);

        // Act
        List<Order> result = service.getAll();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("order-123", result.get(0).getId());
        assertEquals("order-789", result.get(1).getId());
        verify(repository, times(1)).findAll();
    }

    @Test
    void testGetAllReturnsEmptyList() {
        // Arrange
        when(repository.findAll()).thenReturn(Arrays.asList());

        // Act
        List<Order> result = service.getAll();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetByUser() {
        // Arrange
        List<Order> orders = Arrays.asList(order);
        when(repository.findByUserId("user-456")).thenReturn(orders);

        // Act
        List<Order> result = service.getByUser("user-456");

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("user-456", result.get(0).getUserId());
        verify(repository, times(1)).findByUserId("user-456");
    }

    @Test
    void testGetById() {
        // Arrange
        when(repository.findById("order-123")).thenReturn(Optional.of(order));

        // Act
        Order result = service.getById("order-123");

        // Assert
        assertNotNull(result);
        assertEquals("order-123", result.getId());
        assertEquals("user-456", result.getUserId());
        verify(repository, times(1)).findById("order-123");
    }

    @Test
    void testGetByIdThrowsExceptionWhenNotFound() {
        // Arrange
        when(repository.findById("nonexistent")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> service.getById("nonexistent"));
        verify(repository, times(1)).findById("nonexistent");
    }

    @Test
    void testGetByIdExceptionMessage() {
        // Arrange
        String nonExistentId = "missing-order";
        when(repository.findById(nonExistentId)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, 
            () -> service.getById(nonExistentId));
        assertTrue(exception.getMessage().contains("Order not found"));
    }

    @Test
    void testUpdateStatus() {
        // Arrange
        when(repository.findById("order-123")).thenReturn(Optional.of(order));
        when(repository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Order result = service.updateStatus("order-123", "CONFIRMED");

        // Assert
        assertEquals("CONFIRMED", result.getStatus());
        verify(repository).findById("order-123");
        verify(repository).save(any(Order.class));
    }

    @Test
    void testUpdateStatusPreservesOtherFields() {
        // Arrange
        Order existing = new Order();
        existing.setId("order-123");
        existing.setUserId("user-456");
        existing.setStatus("PLACED");
        existing.setTotalAmount(25.98);

        when(repository.findById("order-123")).thenReturn(Optional.of(existing));
        when(repository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Order result = service.updateStatus("order-123", "CONFIRMED");

        // Assert
        assertEquals("CONFIRMED", result.getStatus());
        assertEquals("order-123", result.getId());
        assertEquals("user-456", result.getUserId());
        assertEquals(25.98, result.getTotalAmount());
    }

    @Test
    void testCancel() {
        // Arrange
        when(repository.findById("order-123")).thenReturn(Optional.of(order));
        when(repository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        service.cancel("order-123");

        // Assert
        ArgumentCaptor<Order> captor = ArgumentCaptor.forClass(Order.class);
        verify(repository).save(captor.capture());
        assertEquals("CANCELLED", captor.getValue().getStatus());
    }

    @Test
    void testCancelThrowsExceptionWhenNotFound() {
        // Arrange
        when(repository.findById("nonexistent")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> service.cancel("nonexistent"));
    }

    @Test
    void testCheckoutWithZeroItems() {
        // Arrange
        Order checkoutOrder = new Order();
        checkoutOrder.setUserId("user-456");
        checkoutOrder.setItems(Arrays.asList());

        Order savedOrder = new Order();
        savedOrder.setId("order-123");
        savedOrder.setTotalAmount(0.0);
        savedOrder.setStatus("PLACED");

        when(repository.save(any(Order.class))).thenReturn(savedOrder);

        // Act
        Order result = service.checkout(checkoutOrder);

        // Assert
        assertEquals(0.0, result.getTotalAmount());
    }
}
