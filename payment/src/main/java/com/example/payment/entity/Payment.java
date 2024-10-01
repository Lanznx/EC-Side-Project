package com.example.payment.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import java.util.UUID;
import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID paymentId;

    private UUID orderId;
    private String userId;
    private UUID productId;
    private double amount;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    private LocalDateTime paymentDate;

    public UUID getPaymentId() {
        return paymentId;
    }

    public Payment setPaymentId(UUID paymentId) {
        this.paymentId = paymentId;
        return this;
    }

    public UUID getOrderId() {
        return orderId;
    }

    public Payment setOrderId(UUID orderId) {
        this.orderId = orderId;
        return this;
    }

    public String getUserId() {
        return userId;
    }

    public Payment setUserId(String userId) {
        this.userId = userId;
        return this;
    }

    public UUID getProductId() {
        return productId;
    }

    public Payment setProductId(UUID productId) {
        this.productId = productId;
        return this;
    }

    public double getAmount() {
        return amount;
    }

    public Payment setAmount(double amount) {
        this.amount = amount;
        return this;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public Payment setStatus(PaymentStatus status) {
        this.status = status;
        return this;
    }

    public LocalDateTime getPaymentDate() {
        return paymentDate;
    }

    public Payment setPaymentDate(LocalDateTime paymentDate) {
        this.paymentDate = paymentDate;
        return this;
    }
}