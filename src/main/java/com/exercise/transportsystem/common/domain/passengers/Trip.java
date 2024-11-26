package com.exercise.transportsystem.common.domain.passengers;

import lombok.Data;

import java.time.LocalTime;

@Data
public class Trip {
    private String id;
    private String routeId;
    private String vehicleId;
    private LocalTime startTs;
    private LocalTime   endTs;
    private double      fare; // Implicit currency
}
