package com.know_wave.comma.comma_backend.arduino.dto;

import jakarta.validation.constraints.NotEmpty;

public class OrderRequest {

    @NotEmpty(message = "{Required}")
    private String accountId;

    @NotEmpty(message = "{Required}")
    private String purpose;

    @NotEmpty(message = "{Required}")
    private String description;

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
