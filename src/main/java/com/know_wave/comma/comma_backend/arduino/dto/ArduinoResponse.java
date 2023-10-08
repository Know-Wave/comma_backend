package com.know_wave.comma.comma_backend.arduino.dto;

import com.know_wave.comma.comma_backend.arduino.entity.Arduino;

import java.util.List;

public class ArduinoResponse {

    public ArduinoResponse(Long arduinoId, String arduinoName, int arduinoCount, int arduinoOriginalCount, String arduinoDescription, List<String> arduinoCategories) {
        this.arduinoId = arduinoId;
        this.arduinoName = arduinoName;
        this.arduinoCount = arduinoCount;
        this.arduinoOriginalCount = arduinoOriginalCount;
        this.arduinoDescription = arduinoDescription;
        this.arduinoCategories = arduinoCategories;
    }

    public static ArduinoResponse of(Arduino arduino) {
        return new ArduinoResponse(
                arduino.getId(),
                arduino.getName(),
                arduino.getCount(),
                arduino.getOriginalCount(),
                arduino.getDescription(),
                arduino.getCategories());
    }

    private final Long arduinoId;

    private final String arduinoName;

    private final int arduinoCount;

    private final int arduinoOriginalCount;

    private final String arduinoDescription;

    private final List<String> arduinoCategories;

    public Long getArduinoId() {
        return arduinoId;
    }

    public String getArduinoName() {
        return arduinoName;
    }

    public int getArduinoCount() {
        return arduinoCount;
    }

    public int getArduinoOriginalCount() {
        return arduinoOriginalCount;
    }

    public String getArduinoDescription() {
        return arduinoDescription;
    }

    public List<String> getArduinoCategories() {
        return arduinoCategories;
    }
}
