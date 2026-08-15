package com.booknest.orderservice.controller;

import com.booknest.orderservice.model.Order;
import com.booknest.orderservice.model.OrderItem;
import com.booknest.orderservice.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderControllerTest {

    @Mock
    private OrderService service;

    @InjectMocks
    private OrderController controller;

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
        order.setTotalAmount(25.98);
        order.setStatus("PLACED");
        order.setDeliveryAddress("123 Main St");
    }

    @Test
    void testCheckout() {
        when(service.checkout(any(Order.class))).thenReturn(order);

        Order result = controller.checkout(order).getBody();

        assertNotNull(result);
        assertEquals("order-123", result.getId());
        assertEquals("user-456", result.getUserId());
        assertEquals("PLACED", result.getStatus());
        assertEquals(25.98, result.getTotalAmount());
        verify(service, times(1)).checkout(any(Order.class));
    }

    @Test
    void testGetAllOrders() {
        Order order2 = new Order();
        order2.setId("order-789");
        order2.setUserId("user-789");
        order2.setStatus("CONFIRMED");

        List<Order> orders = Arrays.asList(order, order2);
        when(service.getAll()).thenReturn(orders);

        List<Order> result = controller.getAll(null);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("order-123", result.get(0).getId());
        assertEquals("order-789", result.get(1).getId());
        verify(service, times(1)).getAll();
    }

    @Test
    void testGetOrdersByUserId() {
        List<Order> orders = Arrays.asList(order);
        when(service.getByUser("user-456")).thenReturn(orders);

        List<Order> result = controller.getAll("user-456");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("user-456", result.get(0).getUserId());
        verify(service, times(1)).getByUser("user-456");
    }

    @Test
    void testGetOrderById() {
        when(service.getById("order-123")).thenReturn(order);

        Order result = controller.getById("order-123");

        assertNotNull(result);
        assertEquals("order-123", result.getId());
        assertEquals("user-456", result.getUserId());
        assertEquals("PLACED", result.getStatus());
        verify(service, times(1)).getById("order-123");
    }

    @Test
    void testUpdateOrderStatus() {
        Order updated = new Order();
        updated.setId("order-123");
        updated.setStatus("CONFIRMED");

        when(service.updateStatus("order-123", "CONFIRMED")).thenReturn(updated);

        Map<String, String> statusBody = new HashMap<>();
        statusBody.put("status", "CONFIRMED");

        Order result = controller.updateStatus("order-123", statusBody);

        assertNotNull(result);
        assertEquals("CONFIRMED", result.getStatus());
        verify(service, times(1)).updateStatus("order-123", "CONFIRMED");
    }

    @Test
    void testCancelOrder() {
        controller.cancel("order-123");

        verify(service, times(1)).cancel("order-123");
    }
}

