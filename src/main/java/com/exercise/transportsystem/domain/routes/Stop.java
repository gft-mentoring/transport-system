package com.exercise.transportsystem.domain.routes;

import com.exercise.transportsystem.domain.Coordinates;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;

import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Data
@With
@AllArgsConstructor
public class Stop {

    UUID stopId;
    String name;
    Coordinates coordinates;
    List<LocalTime> arrivalTimes;
}
