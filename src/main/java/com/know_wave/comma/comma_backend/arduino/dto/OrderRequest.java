package com.know_wave.comma.comma_backend.arduino.dto;

import jakarta.validation.constraints.NotEmpty;

public class OrderRequest {

    @NotEmpty(message = "{Required}")
    private String subject;

    @NotEmpty(message = "{Required}")
    private String description;

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
