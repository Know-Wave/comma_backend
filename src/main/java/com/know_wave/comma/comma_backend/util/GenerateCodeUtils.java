package com.know_wave.comma.comma_backend.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ThreadLocalRandom;

public class GenerateCodeUtils {

    public static int getSixRandomCode() {
        return ThreadLocalRandom.current().nextInt(888888) + 111111;
    }

    public static String getCodeByIdWithDate(String accountId) {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMddHHmmss");
        String dateString = now.format(formatter);
        return accountId + dateString;
    }
}
