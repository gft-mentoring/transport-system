package com.exercise.transportsystem.common.domain.routes;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class Route {

    UUID id;
    String name;
    List<Stop> stops;
    Schedule schedule;

}
