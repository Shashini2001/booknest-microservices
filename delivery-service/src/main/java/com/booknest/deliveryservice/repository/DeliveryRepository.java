package com.booknest.deliveryservice.repository;

import com.booknest.deliveryservice.model.Delivery;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface DeliveryRepository extends MongoRepository<Delivery, String> {
    Optional<Delivery> findByOrderId(String orderId);
}
