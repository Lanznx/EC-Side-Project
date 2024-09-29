package com.example.productorder.MQ;

import java.util.UUID;

public class PaymentFailedEvent extends PaymentEvent {
    public PaymentFailedEvent(UUID orderId) {
        super(orderId);
    }
}