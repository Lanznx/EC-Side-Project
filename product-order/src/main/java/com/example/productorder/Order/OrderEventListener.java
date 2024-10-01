package com.example.productorder.Order;

 import com.example.productorder.Order.entity.Order;

import com.example.productorder.Order.entity.OrderStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class OrderEventListener {

    @Autowired
    private OrderService orderService;

    @KafkaListener(topics = "payment-successful")
    public void handlePaymentSuccessfulEvent(String orderId) {
        orderService.updateOrderStatus(UUID.fromString(orderId), OrderStatus.PAID);
    }

    @KafkaListener(topics = "payment-failed")
    public void handlePaymentFailedEvent(String orderId) {
        orderService.updateOrderStatus(UUID.fromString(orderId), OrderStatus.CANCELED);

        Order order = orderService.getOrderById(UUID.fromString(orderId));
        orderService.releaseStock(order.getProductId(), order.getQuantity());
    }
}