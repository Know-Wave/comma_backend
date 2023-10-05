package com.know_wave.comma.comma_backend.arduino.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class OrderDescription {

    @Id
    @GeneratedValue
    @Column(name = "order_description_id")
    private Long id;

    private String description;

    private String orderNumber;
}
