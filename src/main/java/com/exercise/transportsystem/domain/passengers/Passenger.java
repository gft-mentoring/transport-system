package com.exercise.transportsystem.domain.passengers;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Accessors(chain = true)
@Data
public class Passenger {

    private String email;
    private String name;
    private String surname;
    private List<Trip> trips;

}
