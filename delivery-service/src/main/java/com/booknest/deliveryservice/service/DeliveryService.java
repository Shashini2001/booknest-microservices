package com.booknest.deliveryservice.service;

import com.booknest.deliveryservice.model.Delivery;
import com.booknest.deliveryservice.repository.DeliveryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
public class DeliveryService {

    @Autowired
    private DeliveryRepository repository;

    private final Random random = new Random();

    // Called automatically when an OrderPlaced event arrives from RabbitMQ
    public Delivery createForOrder(String orderId) {
        Delivery delivery = new Delivery();
        delivery.setOrderId(orderId);
        delivery.setRiderId("RIDER-" + (100 + random.nextInt(900)));
        delivery.setStatus("ASSIGNED");
        // Starting point - Colombo city centre, as a simple simulation default
        delivery.setCurrentLat(6.9271);
        delivery.setCurrentLng(79.8612);
        delivery.setEta("30 mins");
        return repository.save(delivery);
    }

    public Delivery create(Delivery delivery) {
        return repository.save(delivery);
    }

    public List<Delivery> getAll() {
        return repository.findAll();
    }

    public Delivery getByOrderId(String orderId) {
        return repository.findByOrderId(orderId)
                .orElseThrow(() -> new RuntimeException("No delivery found for order: " + orderId));
    }

    public Delivery update(String orderId, Delivery updates) {
        Delivery existing = getByOrderId(orderId);
        if (updates.getStatus() != null) existing.setStatus(updates.getStatus());
        if (updates.getCurrentLat() != 0) existing.setCurrentLat(updates.getCurrentLat());
        if (updates.getCurrentLng() != 0) existing.setCurrentLng(updates.getCurrentLng());
        if (updates.getEta() != null) existing.setEta(updates.getEta());
        return repository.save(existing);
    }

    public void cancel(String orderId) {
        Delivery existing = getByOrderId(orderId);
        repository.delete(existing);
    }
}
