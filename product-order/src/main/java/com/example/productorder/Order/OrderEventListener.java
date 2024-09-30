package com.example.productorder.Order;

import com.example.productorder.MQ.PaymentFailedEvent;
import com.example.productorder.MQ.PaymentSuccessfulEvent;
import com.example.productorder.Order.entity.Order;

import com.example.productorder.Order.entity.OrderStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class OrderEventListener {

    @Autowired
    private OrderService orderService;

    @KafkaListener(topics = "payment-successful")
    public void handlePaymentSuccessfulEvent(PaymentSuccessfulEvent event) {
        orderService.updateOrderStatus(event.getOrderId(), OrderStatus.PAID);
    }

    @KafkaListener(topics = "payment-failed")
    public void handlePaymentFailedEvent(PaymentFailedEvent event) {
        orderService.updateOrderStatus(event.getOrderId(), OrderStatus.CANCELED);

        Order order = orderService.getOrderById(event.getOrderId());
        orderService.releaseStock(order.getProductId(), order.getQuantity());
    }
}