package com.example.inventoryservice.exception;

public class NotEnoughInventoryException extends RuntimeException {

    public NotEnoughInventoryException(String message) {
        super(message);
    }
}
