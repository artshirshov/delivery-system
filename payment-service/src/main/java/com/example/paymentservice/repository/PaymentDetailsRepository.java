package com.example.paymentservice.repository;

import com.example.paymentservice.model.PaymentDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentDetailsRepository extends JpaRepository<PaymentDetails, Long> {

    Optional<PaymentDetails> findByOrderId(Long orderId);
}
