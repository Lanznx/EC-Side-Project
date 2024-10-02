package com.example.payment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.HashMap;
import java.util.UUID;

@RestController
@RequestMapping("/payments")
public class PaymentController {

  @Autowired
  private PaymentService paymentService;

  @PostMapping
  public String processPayment(@RequestBody PaymentRequest paymentRequest) throws IOException {
    String htmlForm =
        paymentService.generateHtmlForm(paymentRequest.getOrderId(), paymentRequest.getUserId(),
            paymentRequest.getAmount());

    return htmlForm;
  }

  @GetMapping("/success")
  public ResponseEntity<byte[]> getPaymentPage() throws IOException {
    Path path = Paths.get(new ClassPathResource("static/payment.html").getURI());
    byte[] content = Files.readAllBytes(path);
    HttpHeaders headers = new HttpHeaders();
    headers.add(HttpHeaders.CONTENT_TYPE, "text/html; charset=UTF-8");
    return new ResponseEntity<>(content, headers, HttpStatus.OK);
  }

  @PostMapping("/callback")
  public void processCallback(@RequestBody String callback) {
    CallbackRequest callbackRequest = CallbackRequest.fromUrlEncodedString(callback);
    System.out.println(callbackRequest);
    paymentService.processCallbackResult(callbackRequest.getStatus(), callbackRequest.getTradeInfo(), callbackRequest.getTradeSha());
  }
}


class PaymentRequest {
  private UUID orderId;
  private String userId;
  private Integer amount;

  public UUID getOrderId() {
    return orderId;
  }

  public void setOrderId(UUID orderId) {
    this.orderId = orderId;
  }

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public Integer getAmount() {
    return amount;
  }

  public void setAmount(Integer amount) {
    this.amount = amount;
  }
}



class CallbackRequest {
  private String status;
  private String merchantID;
  private String version;
  private String tradeInfo;
  private String tradeSha;

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getMerchantID() {
    return merchantID;
  }

  public void setMerchantID(String merchantID) {
    this.merchantID = merchantID;
  }

  public String getVersion() {
    return version;
  }

  public void setVersion(String version) {
    this.version = version;
  }

  public String getTradeInfo() {
    return tradeInfo;
  }

  public void setTradeInfo(String tradeInfo) {
    this.tradeInfo = tradeInfo;
  }

  public String getTradeSha() {
    return tradeSha;
  }

  public void setTradeSha(String tradeSha) {
    this.tradeSha = tradeSha;
  }

  @Override
  public String toString() {
    return "CallbackDTO{" + "status='" + status + '\'' + ", merchantID='" + merchantID + '\'' + ", version='" + version + '\'' + ", tradeInfo='" + tradeInfo + '\'' + ", tradeSha='" + tradeSha + '\'' + '}';
  }

  public static CallbackRequest fromUrlEncodedString(String callback) {
    Map<String, String> params = new HashMap<>();
    String[] pairs = callback.split("&");
    for (String pair : pairs) {
      String[] keyValue = pair.split("=");
      try {
        String key = URLDecoder.decode(keyValue[0], "UTF-8");
        String value = URLDecoder.decode(keyValue[1], "UTF-8");
        params.put(key, value);
      } catch (UnsupportedEncodingException e) {
        e.printStackTrace();
      }
    }

    CallbackRequest callbackRequest = new CallbackRequest();
    callbackRequest.setStatus(params.get("Status"));
    callbackRequest.setMerchantID(params.get("MerchantID"));
    callbackRequest.setVersion(params.get("Version"));
    callbackRequest.setTradeInfo(params.get("TradeInfo"));
    callbackRequest.setTradeSha(params.get("TradeSha"));

    return callbackRequest;
  }
}