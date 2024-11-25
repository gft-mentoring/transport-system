package com.exercise.transportsystem.vehicles.domain.exception;

import com.exercise.transportsystem.domain.exception.DomainException;

public class VehiclesDomainException extends DomainException {

    public VehiclesDomainException(String msg, int statusCode) {
        super(msg, statusCode);
    }
}
