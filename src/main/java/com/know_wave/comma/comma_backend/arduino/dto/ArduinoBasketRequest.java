package com.know_wave.comma.comma_backend.arduino.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;

public class ArduinoBasketRequest {

    @NotEmpty(message = "{Required}")
    private Long arduinoId;
    @NotEmpty(message = "{Required}")
    private String accountId;

    @Min(value = 1, message = "{NotAcceptable.range}")
    private int count;

    public Long getArduinoId() {
        return arduinoId;
    }

    public void setArduinoId(Long arduinoId) {
        this.arduinoId = arduinoId;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
