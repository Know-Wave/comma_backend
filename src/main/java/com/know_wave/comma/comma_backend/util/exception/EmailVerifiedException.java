package com.know_wave.comma.comma_backend.util.exception;

public class EmailVerifiedException extends RuntimeException{

    public EmailVerifiedException() {
        super();
    }

    public EmailVerifiedException(String message) {
        super(message);
    }
}