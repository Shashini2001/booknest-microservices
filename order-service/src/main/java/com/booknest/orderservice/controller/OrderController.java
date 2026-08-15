package com.booknest.orderservice.controller;

import com.booknest.orderservice.model.Order;
import com.booknest.orderservice.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService service;

    @PostMapping("/checkout")
    public ResponseEntity<Order> checkout(@RequestBody Order order) {
        return ResponseEntity.status(201).body(service.checkout(order));
    }

    @GetMapping
    public List<Order> getAll(@RequestParam(required = false) String userId) {
        if (userId != null) {
            return service.getByUser(userId);
        }
        return service.getAll();
    }

    @GetMapping("/{id}")
    public Order getById(@PathVariable String id) {
        return service.getById(id);
    }

    @PutMapping("/{id}/status")
    public Order updateStatus(@PathVariable String id, @RequestBody Map<String, String> body) {
        return service.updateStatus(id, body.get("status"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancel(@PathVariable String id) {
        service.cancel(id);
        return ResponseEntity.noContent().build();
    }
}
