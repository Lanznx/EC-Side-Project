package com.example.payment;

import java.util.UUID;

public class UUIDUtil {

    public static String getFirst30CharsAndConvertDash(UUID uuid) {
        return uuid.toString().substring(0, 30).replace("-", "_");
    }

    public static String revertUnderline(String uuid) {
        return uuid.replace("_", "-");
    }
}