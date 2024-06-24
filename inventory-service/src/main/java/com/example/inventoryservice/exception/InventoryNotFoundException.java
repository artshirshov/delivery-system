package com.example.inventoryservice.exception;

public class InventoryNotFoundException extends Exception {

    public InventoryNotFoundException(String message) {
        super(message);
    }
}
