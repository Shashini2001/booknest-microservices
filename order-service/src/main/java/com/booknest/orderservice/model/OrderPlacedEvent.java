package com.booknest.orderservice.model;

import java.io.Serializable;

// Small message sent to RabbitMQ when an order is placed.
// Delivery Service has its own matching copy of this class.
public class OrderPlacedEvent implements Serializable {
    private String orderId;
    private String userId;
    private double totalAmount;

    public OrderPlacedEvent() {
    }

    public OrderPlacedEvent(String orderId, String userId, double totalAmount) {
        this.orderId = orderId;
        this.userId = userId;
        this.totalAmount = totalAmount;
    }

    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }
}
