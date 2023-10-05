package com.know_wave.comma.comma_backend.arduino.dto;

public class ArduinoResponse {

    public ArduinoResponse(Long id, String name, int count, int originalCount, String description) {
        this.id = id;
        this.name = name;
        this.count = count;
        this.originalCount = originalCount;
        this.description = description;
    }

    private Long id;

    private String name;

    private int count;

    private int originalCount;

    private String description;

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
}
