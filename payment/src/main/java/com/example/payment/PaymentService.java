package com.example.payment;

import com.example.payment.entity.Payment;
import com.example.payment.entity.PaymentStatus;
import com.example.payment.provider.newwebpay.NewebpayClient;
import com.example.payment.provider.newwebpay.NewebpayRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class PaymentService {

  @Autowired
  private PaymentRepository paymentRepository;

  @Autowired
  private KafkaTemplate<String, String> kafkaTemplate;

  @Transactional
  public String generateHtmlForm(UUID orderId, String userId, UUID productId, double amount)
      throws IOException {
    Payment payment = new Payment().setOrderId(orderId).setUserId(userId).setProductId(productId)
        .setAmount(amount).setStatus(PaymentStatus.PENDING).setPaymentDate(LocalDateTime.now());
    paymentRepository.save(payment);

    NewebpayClient client = new NewebpayClient();
    NewebpayRequest request = new NewebpayRequest().setRespondType("String")
        .setTimeStamp(String.valueOf(System.currentTimeMillis() / 1000)).setVersion("2.0")
        .setMerchantOrderNo(orderId.toString()).setAmt(String.valueOf(amount)).setItemDesc("test")
        .setNotifyURL(
            "https://9eb8-2401-e180-8d02-4f4f-4103-45ff-e3e6-f07.ngrok-free.app/payments/callback")
        .setMerchantID(client.merchantId);

    String htmlForm = client.generateHtmlForm(request.toMap());

    return htmlForm;
    //    boolean paymentSuccessful = true;
    //
    //    if (paymentSuccessful) {
    //      payment.setStatus(PaymentStatus.SUCCESSFUL);
    //      paymentRepository.save(payment);
    //      kafkaTemplate.send("payment-successful", orderId.toString());
    //
    //    } else {
    //      payment.setStatus(PaymentStatus.FAILED);
    //      paymentRepository.save(payment);
    //      kafkaTemplate.send("payment-failed", orderId.toString());
    //    }
  }
}
