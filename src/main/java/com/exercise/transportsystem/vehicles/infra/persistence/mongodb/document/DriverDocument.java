package com.exercise.transportsystem.vehicles.infra.persistence.mongodb.document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;


@Document("drivers")
@Data
@Accessors(chain = true)
@AllArgsConstructor
public class DriverDocument {

    @Id
    @Indexed(unique = true)
    private String id; // email
    private String name;
    private String contact;
    private String vehicleId;
}
