package com.exercise.transportsystem.routes.domain.exception;

import com.exercise.transportsystem.domain.exception.DomainException;

public class RoutesDomainException extends DomainException {

    public RoutesDomainException(String msg, int statusCode) {
        super(msg, statusCode);
    }
}
