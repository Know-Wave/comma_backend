package com.know_wave.comma.comma_backend.arduino.dto.basket;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class BasketRequest {

    @NotNull(message = "{Required}")
    @Min(value = 1, message = "{NotAcceptable.range}")
    private Long arduinoId;

    @Min(value = 1, message = "{NotAcceptable.range}")
    @Max(value = 10, message = "{NotAcceptable.range}")
    private int count;

    public Long getArduinoId() {
        return arduinoId;
    }

    public void setArduinoId(Long arduinoId) {
        this.arduinoId = arduinoId;
    }


    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
