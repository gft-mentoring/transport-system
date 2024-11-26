package com.exercise.transportsystem.passengers.domain.exception;

import com.exercise.transportsystem.common.domain.exception.DomainException;

public class PassengersDomainException extends DomainException {

    public PassengersDomainException(String msg, int statusCode) {
        super(msg, statusCode);
    }
}
