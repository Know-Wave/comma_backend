package com.know_wave.comma.comma_backend.arduino.dto.order;

import java.time.LocalDateTime;

public class OrderResponse {

    public OrderResponse(String accountId, String orderDescription, LocalDateTime orderDate, String orderStatus, String orderCode, String subject, String cancelReason) {
        this.accountId = accountId;
        this.orderDescription = orderDescription;
        this.orderDate = orderDate;
        this.orderStatus = orderStatus;
        this.orderCode = orderCode;
        this.subject = subject;
        this.cancelReason = cancelReason;
    }

    private final String accountId;
    private final String orderDescription;
    private final LocalDateTime orderDate;
    private final String orderStatus;
    private final String orderCode;
    private final String subject;
    private final String cancelReason;

    public String getCancelReason() {
        return cancelReason;
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

    public String getSubject() {
        return subject;
    }
}
