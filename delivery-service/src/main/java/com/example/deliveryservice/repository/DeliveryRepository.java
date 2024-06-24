package com.example.deliveryservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.deliveryservice.model.Delivery;

@Repository
public interface DeliveryRepository extends JpaRepository<Delivery, Long> {
}
