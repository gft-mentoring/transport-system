package com.exercise.transportsystem.passengers.infra.persistence.mongodb.document;

import com.exercise.transportsystem.domain.passengers.Trip;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;
import nonapi.io.github.classgraph.json.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document("passengers")
@Data

@Accessors(chain = true)
@AllArgsConstructor
public class PassengerDocument {

    @Id
    private String id; // email
    private String name;
    private String surname;
    private List<Trip> trips;

}
