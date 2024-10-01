package com.example.payment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@RestController
@RequestMapping("/payments")
public class PaymentController {

  @Autowired
  private PaymentService paymentService;

  @PostMapping
  public String processPayment(@RequestBody PaymentRequest paymentRequest) throws IOException {
    String htmlForm = paymentService.generateHtmlForm(paymentRequest.getOrderId(), paymentRequest.getUserId(),
        paymentRequest.getProductId(), paymentRequest.getAmount());

    return htmlForm;
  }

  @GetMapping("/{orderId}")
  public ResponseEntity<byte[]> getPaymentPage(@PathVariable UUID orderId) throws IOException {
    Path path = Paths.get(new ClassPathResource("static/payment.html").getURI());
    byte[] content = Files.readAllBytes(path);
    HttpHeaders headers = new HttpHeaders();
    headers.add(HttpHeaders.CONTENT_TYPE, "text/html; charset=UTF-8");
    return new ResponseEntity<>(content, headers, HttpStatus.OK);
  }

  @PostMapping("/callback")
  public void processCallback(@RequestBody String callback) {
    // print anything related to callback
    System.out.println(callback);
    System.out.println(callback);
    System.out.println(callback);
    System.out.println(callback);

  }
}


class PaymentRequest {
  private UUID orderId;
  private UUID productId;
  private String userId;
  private double amount;

  public UUID getOrderId() {
    return orderId;
  }

  public void setOrderId(UUID orderId) {
    this.orderId = orderId;
  }

  public UUID getProductId() {
    return productId;
  }

  public void setProductId(UUID productId) {
    this.productId = productId;
  }

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public double getAmount() {
    return amount;
  }

  public void setAmount(double amount) {
    this.amount = amount;
  }
}


