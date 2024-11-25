package com.exercise.transportsystem.vehicles.infra.persistence.mongodb.document;

import com.exercise.transportsystem.domain.Coordinates;
import com.exercise.transportsystem.domain.vehicle.VehicleStatus;
import com.exercise.transportsystem.domain.vehicle.VehicleType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalTime;

@Document("vehicles")
@Data
@ToString
@Accessors(chain = true)
@AllArgsConstructor
public class VehicleDocument {

    @Id
    @Indexed(unique = true)
    private String id;

    private String driverEmail;
    private int capacity;
    private VehicleStatus status;
    private VehicleType type;
    private Coordinates coordinates;
    private LocalTime lastUpdated;
    private LocalDate lastMaintenance;

}
