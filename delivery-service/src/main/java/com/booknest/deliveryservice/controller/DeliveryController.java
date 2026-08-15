package com.booknest.deliveryservice.controller;

import com.booknest.deliveryservice.model.Delivery;
import com.booknest.deliveryservice.service.DeliveryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/deliveries")
public class DeliveryController {

    @Autowired
    private DeliveryService service;

    // Manual creation - normally deliveries are auto-created by the
    // OrderPlaced RabbitMQ event, this endpoint is here for direct
    // CRUD testing and for the assignment's "Create" requirement.
    @PostMapping
    public ResponseEntity<Delivery> create(@RequestBody Delivery delivery) {
        return ResponseEntity.status(201).body(service.create(delivery));
    }

    @GetMapping
    public List<Delivery> getAll() {
        return service.getAll();
    }

    @GetMapping("/{orderId}")
    public Delivery getByOrderId(@PathVariable String orderId) {
        return service.getByOrderId(orderId);
    }

    @PutMapping("/{orderId}")
    public Delivery update(@PathVariable String orderId, @RequestBody Delivery updates) {
        return service.update(orderId, updates);
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<Void> cancel(@PathVariable String orderId) {
        service.cancel(orderId);
        return ResponseEntity.noContent().build();
    }
}
