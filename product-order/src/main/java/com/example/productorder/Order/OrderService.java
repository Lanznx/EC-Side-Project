package com.example.productorder.Order;

import com.example.productorder.Order.entity.Order;
import com.example.productorder.Order.entity.OrderStatus;
import com.example.productorder.Product.ProductService;
import com.example.productorder.Product.entity.Product;
import com.example.productorder.exception.InsufficientStockException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductService productService;

    @Transactional
    public UUID createOrder(String userId, UUID productId, int quantity) {
        if (!productService.updateStock(productId, quantity)) {
            throw new InsufficientStockException("Not enough stock for " + productId);
        }

        Product product = productService.getProductById(productId);
        Order order = new Order();
        order.setUserId(userId);
        order.setProductId(productId);
        order.setQuantity(quantity);
        order.setTotalPrice(product.getPrice() * quantity);
        order.setStatus(OrderStatus.PENDING_PAYMENT);
        orderRepository.save(order);

        return order.getOrderId();
    }

    public Order getOrderById(UUID orderId) {
        return orderRepository.findById(orderId).orElse(null);
    }

    @Transactional
    public void updateOrderStatus(UUID orderId, OrderStatus status) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("Order not found"));
        order.setStatus(status);
        orderRepository.save(order);
    }

    @Transactional
    public void releaseStock(UUID productId, int quantity) {
        productService.updateStock(productId, -quantity);
    }
}