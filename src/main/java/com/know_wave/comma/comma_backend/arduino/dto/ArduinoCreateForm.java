package com.know_wave.comma.comma_backend.arduino.dto;

import com.know_wave.comma.comma_backend.arduino.entity.Arduino;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class ArduinoCreateForm {

    @NotEmpty(message = "{Required}")
    private String arduinoName;

    @NotNull(message = "{Required}")
    @Min(value = 0, message = "{NotAcceptable.range}")
    private int count;

    @NotNull(message = "{Required}")
    @Min(value = 0, message = "{NotAcceptable.range}")
    private int originalCount;

    @NotEmpty(message = "{Required}")
    private String description;

    @NotEmpty(message = "{Required}")
    private List<CategoryDto> categories;

    public Arduino toEntity() {
        return new Arduino(arduinoName, count, originalCount, description);
    }

    public String getArduinoName() {
        return arduinoName;
    }

    public void setArduinoName(String arduinoName) {
        this.arduinoName = arduinoName;
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

    public List<Long> getCategories() {
        return categories.stream().map(CategoryDto::getCategoryId).toList();
    }

    public void setCategories(List<CategoryDto> categories) {
        this.categories = categories;
    }
}
