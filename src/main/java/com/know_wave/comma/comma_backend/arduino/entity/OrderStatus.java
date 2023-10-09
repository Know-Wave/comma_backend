package com.know_wave.comma.comma_backend.arduino.entity;

public enum OrderStatus {

    APPLIED("신청 완료"),
    PREPARING("준비 중"),
    CANCELLATION_REQUEST("주문 취소 요청"),
    REJECTED("요청 거부"),
    CANCELED("주문 취소"),
    READY("준비 완료"),
    RECEIVED_COMPLETED("수령 완료");

    private final String value;

    OrderStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
