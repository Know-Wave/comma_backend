package com.know_wave.comma.comma_backend.arduino.dto;

import com.know_wave.comma.comma_backend.arduino.entity.Arduino;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class ArduinoCreateForm {

    @NotEmpty(message = "{NotEmpty.name}")
    private String name;

    @NotNull(message = "{NotNull.count}")
    @Min(value = 0, message = "{NotAcceptable.range}")
    private int count;

    @NotNull(message = "{NotNull.originalCount}")
    @Min(value = 0, message = "{NotAcceptable.range}")
    private int originalCount;

    @NotEmpty(message = "{NotEmpty.description}")
    private String description;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getOriginalCount() {
        return originalCount;
    }

    public void setOriginalCount(int originalCount) {
        this.originalCount = originalCount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Arduino toEntity() {
        return new Arduino(name, count, originalCount, description);
    }
}
