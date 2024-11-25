package com.exercise.transportsystem.routes.infra.persistence.mongodb.document;


import com.exercise.transportsystem.domain.routes.Schedule;
import com.exercise.transportsystem.domain.routes.Stop;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document("routes")
@Data
@Accessors(chain = true)
@AllArgsConstructor
public class RouteDocument {

    @Id
    @Indexed(unique = true)
    private String id; // uuid
    private String name;
    private List<Stop> stops;
    private Schedule schedule;
}
