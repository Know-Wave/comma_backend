package com.know_wave.comma.comma_backend.account.dto;

import com.know_wave.comma.comma_backend.account.entity.AcademicMajor;
import com.know_wave.comma.comma_backend.account.entity.AcademicStatus;
import com.know_wave.comma.comma_backend.account.entity.Account;
import com.know_wave.comma.comma_backend.util.StringStorage;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;
import org.springframework.security.crypto.password.PasswordEncoder;

import static com.know_wave.comma.comma_backend.util.StringStorage.emailRegex;

public class AccountCreateForm {

    public AccountCreateForm() {
    }

    public AccountCreateForm(String accountId, String password, String name, String email, String academicNumber, AcademicMajor major, AcademicStatus status) {
        this.accountId = accountId;
        this.password = password;
        this.name = name;
        this.email = email;
        this.academicNumber = academicNumber;
        this.major = major;
        this.status = status;
    }

    @NotEmpty(message = "{NotEmpty.accountId}")
    @Length(min = 4, max = 255, message = "{Length}")
    private String accountId;

    @NotEmpty(message = "{NotEmpty.password}")
    @Length(min = 8, max = 255, message = "{Length}")
    private String password;

    @NotEmpty(message = "{NotEmpty.name}")
    @Length(min = 2, max = 8, message = "{Length}")
    private String name;

    @NotEmpty(message = "{NotEmpty.email}")
    @Email(regexp = emailRegex,
            message = "{Email.email}")
    private String email;

    @NotEmpty(message = "{NotEmpty.academicNumber}")
    @Range(min = 20000000, max = 20300000, message = "{Range.academicNumber}")
    private String academicNumber;

    @NotEmpty(message = "{NotEmpty.major}")
    private AcademicMajor major;

    @NotEmpty(message = "{NotEmpty.status}}")
    private AcademicStatus status;

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public AcademicMajor getMajor() {
        return major;
    }

    public void setMajor(AcademicMajor major) {
        this.major = major;
    }

    public AcademicStatus getStatus() {
        return status;
    }

    public void setStatus(AcademicStatus status) {
        this.status = status;
    }
}
