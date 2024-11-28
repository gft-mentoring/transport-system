package com.exercise.transportsystem.common.domain.vehicle;

import com.exercise.transportsystem.common.domain.Coordinates;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Vehicle {

    private String plateId;
    private Driver driver;
    private int capacity;
    private VehicleStatus status;
    private VehicleType type;
    private Coordinates coordinates;
    private LocalTime lastUpdated;
    private LocalDate lastMaintenance;

}
