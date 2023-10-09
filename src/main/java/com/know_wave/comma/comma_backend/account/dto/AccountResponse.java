package com.know_wave.comma.comma_backend.account.dto;

import com.know_wave.comma.comma_backend.account.entity.Account;

public class AccountResponse {

    public AccountResponse(String name, String email, String academicNumber, String academicStatus, String academicMajor, String role) {
        this.name = name;
        this.email = email;
        this.academicNumber = academicNumber;
        this.academicStatus = academicStatus;
        this.academicMajor = academicMajor;
        this.role = role;
    }
    public static AccountResponse of(Account account) {
        return new AccountResponse(
                account.getName(),
                account.getEmail(),
                account.getAcademicNumber(),
                account.getAcademicStatus(),
                account.getAcademicMajor(),
                account.getRole().getGrade()
        );
    }

    private String name;
    private String email;
    private String academicNumber;
    private String academicStatus;

    private String academicMajor;

    private String role;

    public AccountResponse() {
    }

    public String getAcademicStatus() {
        return academicStatus;
    }

    public void setAcademicStatus(String academicStatus) {
        this.academicStatus = academicStatus;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAcademicNumber() {
        return academicNumber;
    }

    public void setAcademicNumber(String academicNumber) {
        this.academicNumber = academicNumber;
    }

    public String getAcademicMajor() {
        return academicMajor;
    }

    public void setAcademicMajor(String academicMajor) {
        this.academicMajor = academicMajor;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
