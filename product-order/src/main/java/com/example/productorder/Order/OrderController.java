package com.example.productorder.Order;

import com.example.productorder.Order.entity.Order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/orders")
public class OrderController {

  @Autowired
  private OrderService orderService;

  @PostMapping()
  public UUID createOrder(@RequestBody Order order) {
      return orderService.createOrder(order.getUserId(), order.getProductId(), order.getQuantity());
  }


  @GetMapping("/{id}")
  public Order getOrderById(@PathVariable UUID id) {
      return orderService.getOrderById(id);
  }
}
