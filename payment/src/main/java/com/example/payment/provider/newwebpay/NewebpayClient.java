package com.example.payment.provider.newwebpay;

import com.google.gson.Gson;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

public class NewebpayClient {

  public String merchantId;
  private String hashKey;
  private String hashIv;
  private String apiUrl;
  private Gson gson;

  public NewebpayClient() throws IOException {
    Properties prop = new Properties();
    try (InputStream input = getClass().getClassLoader().getResourceAsStream("config.properties")) {
      if (input == null) {
        throw new IOException("Unable to find config.properties");
      }
      prop.load(input);
    }
    this.merchantId = prop.getProperty("merchant_id");
    this.hashKey = prop.getProperty("hash_key");
    this.hashIv = prop.getProperty("hash_iv");
    this.apiUrl = prop.getProperty("api_url");
    this.gson = new Gson();
  }

  public String generateHtmlForm(Map<String, String> orderParams) {
    String requestString = generateRequestString(orderParams);
    String encryptedData = encrypt(requestString, hashKey, hashIv);
    String tradeSha = generateTradeSha(encryptedData);
    String htmlForm = "<!DOCTYPE html>" +
                "<html lang='en'>" +
                "<head>" +
                "<meta charset='UTF-8'>" +
                "<meta name='viewport' content='width=device-width, initial-scale=1.0'>" +
                "<title>Payment Form</title>" +
                "</head>" +
                "<body>" +
                "<form method='post' action='" + apiUrl + "'>" +
                "MID: <input name='MerchantID' value='" + merchantId + "' readonly><br>" +
                "Version: <input name='Version' value='2.0' readonly><br>" +
                "TradeInfo: <input name='TradeInfo' value='" + encryptedData + "' readonly><br>" +
                "TradeSha: <input name='TradeSha' value='" + tradeSha + "' readonly><br>" +
                "<input type='submit' value='Submit'>" +
                "</form>" +
                "</body>" +
                "</html>";
    return htmlForm;
  }

  private String encrypt(String data, String key, String iv) {
    try {
      Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
      SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "AES");
      IvParameterSpec ivParameterSpec = new IvParameterSpec(iv.getBytes(StandardCharsets.UTF_8));
      cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
      byte[] encrypted = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
      return bytesToHex(encrypted);
    } catch (Exception e) {
      throw new RuntimeException("Error occurred while encrypting data", e);
    }
  }

  private String bytesToHex(byte[] bytes) {
    StringBuilder sb = new StringBuilder();
    for (byte b : bytes) {
      sb.append(String.format("%02x", b));
    }
    return sb.toString();
  }

  private String generateTradeSha(String encryptedData) {
    String hashString = "HashKey=" + hashKey + "&" + encryptedData + "&HashIV=" + hashIv;
    try {
      MessageDigest digest = MessageDigest.getInstance("SHA-256");
      byte[] hash = digest.digest(hashString.getBytes(StandardCharsets.UTF_8));
      return bytesToHex(hash).toUpperCase();
    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException("Error occurred while generating SHA-256 hash", e);
    }
  }

//  private String postRequest(String url, String json) throws IOException {
//    try (CloseableHttpClient client = HttpClients.createDefault()) {
//      HttpPost post = new HttpPost(url);
//      post.setEntity(new StringEntity(json));
//      post.setHeader("Content-type", "application/json");
//
//      try (CloseableHttpResponse response = client.execute(post)) {
//        return EntityUtils.toString(response.getEntity());
//      }
//    }
//  }

  private String generateRequestString(Map<String, String> params) {
    return params.entrySet().stream().map(entry -> URLEncoder.encode(entry.getKey(),
            StandardCharsets.UTF_8) + "=" + URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8))
        .collect(Collectors.joining("&"));
  }

}

