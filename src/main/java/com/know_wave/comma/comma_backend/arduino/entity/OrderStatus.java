package com.know_wave.comma.comma_backend.arduino.entity;

public enum OrderStatus {

    APPLIED("신청"),
    PREPARING("준비"),
    CANCELLATION_REQUEST("취소 요청"),
    REJECTED("거부"),
    CANCELED("취소"),
    COMPLETED("완료");

    private final String value;

    OrderStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
