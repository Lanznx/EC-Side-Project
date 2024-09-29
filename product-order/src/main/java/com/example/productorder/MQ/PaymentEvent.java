package com.example.productorder.MQ;

import java.util.UUID;

public abstract class PaymentEvent {
    private UUID orderId;

    public PaymentEvent(UUID orderId) {
        this.orderId = orderId;
    }

    public UUID getOrderId() {
        return orderId;
    }
}