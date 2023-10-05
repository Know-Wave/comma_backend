package com.know_wave.comma.comma_backend.arduino.dto;

import java.time.LocalDateTime;
import java.util.List;

public class OrderResponse {

    public OrderResponse(String accountId, String orderDescription, LocalDateTime orderDate, String orderStatus, String orderCode, String purpose, List<Arduino> orderList) {
        this.accountId = accountId;
        this.orderDescription = orderDescription;
        this.orderDate = orderDate;
        this.orderStatus = orderStatus;
        this.orderCode = orderCode;
        this.purpose = purpose;
        this.orderList = orderList;
    }

    private final String accountId;
    private final String orderDescription;
    private final LocalDateTime orderDate;
    private final String orderStatus;
    private final String orderCode;
    private final String purpose;
    private final List<Arduino> orderList;

    public static class Arduino {

        public Arduino(Long arduinoId, String arduinoName, int orderCount) {
            this.arduinoId = arduinoId;
            this.arduinoName = arduinoName;
            this.orderCount = orderCount;
        }

        private final Long arduinoId;
        private final String arduinoName;
        private final int orderCount;

        public Long getArduinoId() {
            return arduinoId;
        }

        public String getArduinoName() {
            return arduinoName;
        }

        public int getOrderCount() {
            return orderCount;
        }
    }

    public String getAccountId() {
        return accountId;
    }

    public String getOrderDescription() {
        return orderDescription;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public String getPurpose() {
        return purpose;
    }

    public List<Arduino> getOrderList() {
        return orderList;
    }
}
