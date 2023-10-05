package com.know_wave.comma.comma_backend.arduino.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class OrderDescription {

    protected OrderDescription() {}

    public OrderDescription(String description, String orderNumber, String purpose) {
        this.description = description;
        this.orderNumber = orderNumber;
        this.purpose = purpose;
    }

    @Id
    @GeneratedValue
    @Column(name = "order_description_id")
    private Long id;

    private String description;

    private String orderNumber;

    private String purpose;

    public String getDescription() {
        return description;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public String getPurpose() {
        return purpose;
    }
}
