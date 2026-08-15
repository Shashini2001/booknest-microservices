package com.booknest.deliveryservice.model;

import java.io.Serializable;

// Must match the shape of OrderPlacedEvent in Order Service - this is how
// two independent microservices agree on an event "contract" without
// sharing code directly.
public class OrderPlacedEvent implements Serializable {
    private String orderId;
    private String userId;
    private double totalAmount;

    public OrderPlacedEvent() {
    }

    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }
}
