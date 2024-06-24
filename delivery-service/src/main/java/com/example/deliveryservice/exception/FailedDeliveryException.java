package com.example.deliveryservice.exception;

public class FailedDeliveryException extends RuntimeException {

    public FailedDeliveryException(String message) {
        super(message);
    }
}
