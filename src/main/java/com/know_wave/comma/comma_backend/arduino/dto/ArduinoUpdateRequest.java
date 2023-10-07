package com.know_wave.comma.comma_backend.arduino.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class ArduinoUpdateRequest {

    private Long arduinoId;

    @NotEmpty(message = "{Required}")
    private String arduinoName;

    @NotNull(message = "{Required}")
    @Min(value = 0, message = "{NotAcceptable.range}")
    private int arduinoCount;

    @NotNull(message = "{Required}")
    @Min(value = 0, message = "{NotAcceptable.range}")
    private int arduinoOriginalCount;

    @NotEmpty(message = "{Required}")
    private String arduinoDescription;

    @NotEmpty(message = "{Required}")
    private List<CategoryDto> arduinoCategories;

    public Long getArduinoId() {
        return arduinoId;
    }

    public void setArduinoId(Long arduinoId) {
        this.arduinoId = arduinoId;
    }

    public String getArduinoName() {
        return arduinoName;
    }

    public void setArduinoName(String arduinoName) {
        this.arduinoName = arduinoName;
    }

    public int getArduinoCount() {
        return arduinoCount;
    }

    public void setArduinoCount(int arduinoCount) {
        this.arduinoCount = arduinoCount;
    }

    public int getArduinoOriginalCount() {
        return arduinoOriginalCount;
    }

    public void setArduinoOriginalCount(int arduinoOriginalCount) {
        this.arduinoOriginalCount = arduinoOriginalCount;
    }

    public String getArduinoDescription() {
        return arduinoDescription;
    }

    public void setArduinoDescription(String arduinoDescription) {
        this.arduinoDescription = arduinoDescription;
    }

    public List<Long> getArduinoCategories() {
        return arduinoCategories.stream().map(CategoryDto::getCategoryId).toList();
    }

    public void setArduinoCategories(List<CategoryDto> arduinoCategories) {
        this.arduinoCategories = arduinoCategories;
    }
}
