package com.booknest.orderservice.service;

import com.booknest.orderservice.config.RabbitMQConfig;
import com.booknest.orderservice.model.Order;
import com.booknest.orderservice.model.OrderPlacedEvent;
import com.booknest.orderservice.repository.OrderRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository repository;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public Order checkout(Order order) {
        double total = order.getItems().stream()
                .mapToDouble(i -> i.getUnitPrice() * i.getQuantity())
                .sum();
        order.setTotalAmount(total);
        order.setStatus("PLACED");

        Order saved = repository.save(order);

        // Publish event so Delivery Service can auto-create a delivery record
        OrderPlacedEvent event = new OrderPlacedEvent(saved.getId(), saved.getUserId(), saved.getTotalAmount());
        rabbitTemplate.convertAndSend(RabbitMQConfig.ORDER_PLACED_QUEUE, event);

        return saved;
    }

    public List<Order> getAll() {
        return repository.findAll();
    }

    public List<Order> getByUser(String userId) {
        return repository.findByUserId(userId);
    }

    public Order getById(String id) {
        return repository.findById(id).orElseThrow(() -> new RuntimeException("Order not found: " + id));
    }

    public Order updateStatus(String id, String status) {
        Order order = getById(id);
        order.setStatus(status);
        return repository.save(order);
    }

    public void cancel(String id) {
        Order order = getById(id);
        order.setStatus("CANCELLED");
        repository.save(order);
    }
}
