package com.exercise.transportsystem.vehicles.api.rest.advice;

import com.exercise.transportsystem.vehicles.domain.exception.VehiclesDomainException;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(VehiclesDomainException.class)
    public final ResponseEntity<ApiErrorResponse> handleDomainException(VehiclesDomainException ex) {

        var apiError = new ApiErrorResponse(ex.getMessage(), ex.getClass().getName(), ex.getStatusCode());

        return new ResponseEntity<>(apiError, HttpStatusCode.valueOf(apiError.statusCode()));

    }


}

record ApiErrorResponse (String message, String kind, int statusCode) {}