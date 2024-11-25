package com.exercise.transportsystem.domain.exception;

public class DomainException extends RuntimeException {

    private int statusCode;

    public DomainException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }

}
