package com.example.orderservice.exception;

public class OrderNotFoundException extends Exception {

    public OrderNotFoundException(Long orderId) {
        super("Could not find order '" + orderId + "'.");
    }
}
