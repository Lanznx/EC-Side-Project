package com.example.payment.provider.newwebpay;

import java.util.Map;

public class NewebpayDto {
    private String merchantID;
    private String respondType;
    private String timeStamp;
    private String version;
    private String merchantOrderNo;
    private String amt;
    private String itemDesc;
    private String notifyURL;

    // Getters and Setters
    public String getMerchantID() {
        return merchantID;
    }

    public NewebpayDto setMerchantID(String merchantID) {
        this.merchantID = merchantID;
        return this;
    }

    public String getRespondType() {
        return respondType;
    }

    public NewebpayDto setRespondType(String respondType) {
        this.respondType = respondType;
        return this;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public NewebpayDto setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
        return this;
    }

    public String getVersion() {
        return version;
    }

    public NewebpayDto setVersion(String version) {
        this.version = version;
        return this;
    }

    public String getMerchantOrderNo() {
        return merchantOrderNo;
    }

    public NewebpayDto setMerchantOrderNo(String merchantOrderNo) {
        this.merchantOrderNo = merchantOrderNo;
        return this;
    }

    public String getAmt() {
        return amt;
    }

    public NewebpayDto setAmt(String amt) {
        this.amt = amt;
        return this;
    }

    public String getItemDesc() {
        return itemDesc;
    }

    public NewebpayDto setItemDesc(String itemDesc) {
        this.itemDesc = itemDesc;
        return this;
    }

    public String getNotifyURL() {
        return notifyURL;
    }

    public NewebpayDto setNotifyURL(String notifyURL) {
        this.notifyURL = notifyURL;
        return this;
    }

    public Map<String, String> toMap() {
        return Map.of("MerchantID", merchantID, "RespondType", respondType, "TimeStamp", timeStamp, "Version", version,
                "MerchantOrderNo", merchantOrderNo, "Amt", amt, "ItemDesc", itemDesc, "NotifyURL", notifyURL);
    }
}