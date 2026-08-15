package com.booknest.deliveryservice.service;

import com.booknest.deliveryservice.config.RabbitMQConfig;
import com.booknest.deliveryservice.model.OrderPlacedEvent;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderPlacedListener {

    @Autowired
    private DeliveryService deliveryService;

    @RabbitListener(queues = RabbitMQConfig.ORDER_PLACED_QUEUE)
    public void onOrderPlaced(OrderPlacedEvent event) {
        System.out.println("Received OrderPlaced event for order: " + event.getOrderId());
        deliveryService.createForOrder(event.getOrderId());
    }
}
