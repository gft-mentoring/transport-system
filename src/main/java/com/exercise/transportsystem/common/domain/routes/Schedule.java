package com.exercise.transportsystem.common.domain.routes;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;

import java.time.LocalTime;

@Data
@AllArgsConstructor
public class Schedule {

    LocalTime startTime;
    LocalTime endTime;
    int frequencyMinutes;

}
