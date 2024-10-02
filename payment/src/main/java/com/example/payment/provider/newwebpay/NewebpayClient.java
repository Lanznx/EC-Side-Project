package com.example.payment.provider.newwebpay;

import com.google.gson.Gson;
import org.apache.commons.codec.binary.Hex;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

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
    String htmlForm =
        "<!DOCTYPE html>" + "<html lang='en'>" + "<head>" + "<meta charset='UTF-8'>" + "<meta name='viewport' content='width=device-width, initial-scale=1.0'>" + "<title>Payment Form</title>" + "</head>" + "<body>" + "<form method='post' action='" + apiUrl + "'>" + "MID: <input name='MerchantID' value='" + merchantId + "' readonly><br>" + "Version: <input name='Version' value='2.0' readonly><br>" + "TradeInfo: <input name='TradeInfo' value='" + encryptedData + "' readonly><br>" + "TradeSha: <input name='TradeSha' value='" + tradeSha + "' readonly><br>" + "<input type='submit' value='Submit'>" + "</form>" + "</body>" + "</html>";
    return htmlForm;
  }

  private String encrypt(String data, String key, String iv) {
    try {
      Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
      SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "AES");
      IvParameterSpec ivParameterSpec = new IvParameterSpec(iv.getBytes(StandardCharsets.UTF_8));
      cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
      byte[] encrypted = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
      return Hex.encodeHexString(encrypted);
    } catch (Exception e) {
      throw new RuntimeException("Error occurred while encrypting data", e);
    }
  }

  private String decrypt(String data, String key, String iv) {
    try {
      Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
      SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "AES");
      IvParameterSpec ivParameterSpec = new IvParameterSpec(iv.getBytes(StandardCharsets.UTF_8));
      cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);
      byte[] decrypted = cipher.doFinal(Hex.decodeHex(data));
      return new String(decrypted, StandardCharsets.UTF_8);
    } catch (Exception e) {
      throw new RuntimeException("Error occurred while decrypting data", e);
    }
  }

  private String bytesToHex(byte[] bytes) {
    StringBuilder sb = new StringBuilder();
    for (byte b : bytes) {
      sb.append(String.format("%02x", b));
    }
    return sb.toString();
  }

  private byte[] hexToBytes(String hex) {
    byte[] bytes = new byte[hex.length() / 2];
    for (int i = 0; i < bytes.length; i++) {
      bytes[i] = (byte) Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
    }
    return bytes;
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

  private String generateRequestString(Map<String, String> params) {
    return params.entrySet().stream().map(entry -> URLEncoder.encode(entry.getKey(),
            StandardCharsets.UTF_8) + "=" + URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8))
        .collect(Collectors.joining("&"));
  }

  public boolean checkIsTradeShaValid(String tradeInfo, String tradeSha) {
    String hashString = "HashKey=" + hashKey + "&" + tradeInfo + "&HashIV=" + hashIv;
    try {
      MessageDigest digest = MessageDigest.getInstance("SHA-256");
      byte[] hash = digest.digest(hashString.getBytes(StandardCharsets.UTF_8));
      String expectedTradeSha = bytesToHex(hash).toUpperCase();
      return expectedTradeSha.equals(tradeSha);
    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException("Error occurred while generating SHA-256 hash", e);
    }
  }

  public NewebpayDto parseTradeInfo(String tradeInfo) {
    String decryptedData = decrypt(tradeInfo, hashKey, hashIv);
    System.out.println(decryptedData);
    System.out.println("---------------- decryptedData ----------------");
    Map<String, String> params = parseQueryString(decryptedData);
    return gson.fromJson(gson.toJson(params), NewebpayDto.class);
  }

  private Map<String, String> parseQueryString(String query) {
    Map<String, String> result = new HashMap<>();
    String[] pairs = query.split("&");
    for (String pair : pairs) {
      int idx = pair.indexOf("=");
      result.put(URLDecoder.decode(pair.substring(0, idx), StandardCharsets.UTF_8),
          URLDecoder.decode(pair.substring(idx + 1), StandardCharsets.UTF_8));
    }
    return result;
  }
}