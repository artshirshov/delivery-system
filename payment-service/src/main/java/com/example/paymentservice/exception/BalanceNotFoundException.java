package com.example.paymentservice.exception;

public class BalanceNotFoundException extends Exception {

    public BalanceNotFoundException(String message) {
        super(message);
    }
}
