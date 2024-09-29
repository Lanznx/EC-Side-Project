package com.example.productorder.MQ;

import java.util.UUID;

public class PaymentSuccessfulEvent extends PaymentEvent {
    public PaymentSuccessfulEvent(UUID orderId) {
        super(orderId);
    }
}