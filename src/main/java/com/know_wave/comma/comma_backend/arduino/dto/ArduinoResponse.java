package com.know_wave.comma.comma_backend.arduino.dto;

import com.know_wave.comma.comma_backend.arduino.entity.Arduino;

import java.util.List;

public class ArduinoResponse {

    public ArduinoResponse(Long id, String name, int count, int originalCount, String description, List<String> categories) {
        this.id = id;
        this.name = name;
        this.count = count;
        this.originalCount = originalCount;
        this.description = description;
        this.categories = categories;
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

    private final Long id;

    private final String name;

    private final int count;

    private final int originalCount;

    private final String description;

    private final List<String> categories;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getCount() {
        return count;
    }

    public int getOriginalCount() {
        return originalCount;
    }

    public String getDescription() {
        return description;
    }

    public List<String> getCategories() {
        return categories;
    }
}
