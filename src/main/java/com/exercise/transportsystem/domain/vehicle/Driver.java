package com.exercise.transportsystem.domain.vehicle;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;

@Data
@With
@AllArgsConstructor
public class Driver {

    String email;
    String name;
    String contact;

}
