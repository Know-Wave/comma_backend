package com.know_wave.comma.comma_backend.arduino.entity;

import jakarta.persistence.*;

@Entity
public class ArduinoCategory {

    protected ArduinoCategory() {}

    public ArduinoCategory(Arduino arduino, Category category) {
        this.arduino = arduino;
        this.category = category;
    }

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "arduino_id")
    private Arduino arduino;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    public Category getCategory() {
        return category;
    }

    public Arduino getArduino() {
        return arduino;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
