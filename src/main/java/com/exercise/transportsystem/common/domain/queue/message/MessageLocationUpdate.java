package com.exercise.transportsystem.common.domain.queue.message;

import com.exercise.transportsystem.common.domain.Coordinates;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class MessageLocationUpdate implements Serializable {

    private String vehicleId;
    private String routeId;
    private Coordinates coordinates;

}
