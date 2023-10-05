package com.know_wave.comma.comma_backend.arduino.entity;

public enum OrderStatus {

    APPLIED("신청"), WAITING("대기"), REJECTED("거부"), CANCELED("취소"), COMPLETED("완료");

    private final String status;

    OrderStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
