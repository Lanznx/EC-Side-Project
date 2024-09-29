package com.example.productorder.exception;

public class InsufficientStockException extends RuntimeException {
    public InsufficientStockException(String message) {
        super(message);
        System.out.println("Not enough stock for " + message);
    }
}