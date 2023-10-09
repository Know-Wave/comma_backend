package com.know_wave.comma.comma_backend.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class GenerateCodeUtils {

    public static int getSixRandomCode() {
        return ThreadLocalRandom.current().nextInt(888888) + 111111;
    }

    public static String getCodeWithDate() {
        String uuid = UUID.randomUUID().toString().substring(0, 8);
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMddHHmmss");
        String dateString = now.format(formatter);
        return uuid + dateString;
    }
}
