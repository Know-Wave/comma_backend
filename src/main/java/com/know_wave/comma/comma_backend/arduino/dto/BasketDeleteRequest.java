package com.know_wave.comma.comma_backend.arduino.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class BasketDeleteRequest {

    @NotNull(message = "{Required}")
    @Min(value = 1, message = "{NotAcceptable.range}")
    private Long arduinoId;

    public Long getArduinoId() {
        return arduinoId;
    }

    public void setArduinoId(Long arduinoId) {
        this.arduinoId = arduinoId;
    }
}
