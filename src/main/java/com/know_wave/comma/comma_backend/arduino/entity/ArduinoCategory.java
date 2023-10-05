package com.know_wave.comma.comma_backend.arduino.entity;

import jakarta.persistence.*;

@Entity
public class ArduinoCategory {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "arduino_id")
    private Arduino arduino;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;
}
