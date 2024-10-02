package com.example.payment;

import com.example.payment.entity.Payment;
import com.example.payment.entity.PaymentStatus;
import com.example.payment.provider.newwebpay.NewebpayClient;
import com.example.payment.provider.newwebpay.NewebpayDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class PaymentService {

  @Autowired
  private PaymentRepository paymentRepository;

  @Autowired
  private KafkaTemplate<String, String> kafkaTemplate;

  private NewebpayClient client = new NewebpayClient();

  public PaymentService() throws IOException {
  }

  @Transactional
  public String generateHtmlForm(UUID orderId, String userId, Integer amount) throws IOException {

    Payment payment =
        new Payment().setOrderId(UUIDUtil.getFirst30CharsAndConvertDash(orderId)).setUserId(userId)
            .setAmount(amount).setStatus(PaymentStatus.PENDING).setPaymentDate(LocalDateTime.now());
    paymentRepository.save(payment);

    NewebpayDto request = new NewebpayDto().setRespondType("String")
        .setTimeStamp(String.valueOf(System.currentTimeMillis() / 1000)).setVersion("2.0")
        .setMerchantOrderNo(UUIDUtil.getFirst30CharsAndConvertDash(orderId))
        .setAmt(String.valueOf(amount)).setItemDesc("test").setNotifyURL(
            "https://9eb8-2401-e180-8d02-4f4f-4103-45ff-e3e6-f07.ngrok-free.app/payments/callback")
        .setMerchantID(client.merchantId);

    String htmlForm = client.generateHtmlForm(request.toMap());

    return htmlForm;
  }

  @Transactional
  public void processCallbackResult(String status, String tradeInfo, String tradeSha) {
    boolean paymentSuccessful = status.equals("SUCCESS");

    boolean isTradeInfoValid = client.checkIsTradeShaValid(tradeInfo, tradeSha);
    NewebpayDto response = client.parseTradeInfo(tradeInfo);
    String orderId = UUIDUtil.revertUnderline(response.getMerchantOrderNo());
    Payment payment = paymentRepository.findByOrderId(UUIDUtil.revertUnderline(orderId));

    if (!isTradeInfoValid) {
      paymentRepository.save(payment.setFailedCode("TRADE_INFO_INVALID"));
      throw new RuntimeException("TradeInfo is not valid");
    }

    if (paymentSuccessful) {
      payment.setStatus(PaymentStatus.SUCCESSFUL);
      kafkaTemplate.send("payment-successful", orderId.toString());
    } else {
      payment.setStatus(PaymentStatus.FAILED).setFailedCode(status);
      kafkaTemplate.send("payment-failed", orderId.toString());
    }
    paymentRepository.save(payment);

  }
}
