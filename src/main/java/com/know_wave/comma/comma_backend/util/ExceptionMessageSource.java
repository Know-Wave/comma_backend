package com.know_wave.comma.comma_backend.util;

import com.know_wave.comma.comma_backend.arduino.entity.OrderStatus;

public class ExceptionMessageSource {

    public static final String ALREADY_VERIFIED_EMAIL = "Already verified email";
    public static final String NOT_VERIFIED_EMAIL = "Not verified email";
    public static final String NOT_FOUND_EMAIL = "Not found email";
    public static final String NOT_EXIST_ACCOUNT = "Not exist account";
    public static final String NOT_EXIST_CATEGORY = "Not exist category";
    public static final String NOT_FOUND_TOKEN = "Not Found token";
    public static final String TEMPERED_TOKEN = "Tempered token";
    public static final String NOT_FOUND_ACCESS_TOKEN = "Not found access token";
    public static final String INVALID_TOKEN = "Invalid token";
    public static final String EXPIRED_REFRESH_TOKEN = "Expired refresh token. login is required";
    public static final String EXPIRED_ACCESS_TOKEN = "Expired access token. re-issuance is required";
    public static final String NOT_FOUND_ARDUINO = "Not found Arduino";
    public static final String ALREADY_EXIST_ARDUINO = "Already exist Arduino";
    public static final String ALREADY_EXIST_VALUE = "Already exist value";
    public static final String PERMISSION_DENIED = "Permission denied";
    public static final String NOT_FOUND_VALUE = "Not found value";
    public static final String NOT_EXIST_VALUE = "Not found value";
    public static final String ALREADY_IN_BASKET = "Already in basket";
    public static final String NOT_ACCEPTABLE_REQUEST = "Not acceptable request";
    public static final String INVALID_VALUE = "Invalid value";
    public static String NOT_ACCEPTABLE_ORDER_STATUS(OrderStatus status) {
        return "이미 " + status.getValue() + "된 상태입니다";
    }
}
