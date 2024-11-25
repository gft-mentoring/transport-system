package com.exercise.transportsystem.vehicles.infra.persistence.mongodb.mapper;

import com.exercise.transportsystem.domain.vehicle.Driver;
import com.exercise.transportsystem.domain.vehicle.Vehicle;
import com.exercise.transportsystem.vehicles.infra.persistence.mongodb.document.DriverDocument;
import com.exercise.transportsystem.vehicles.infra.persistence.mongodb.document.VehicleDocument;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface VehicleMapper {
    @Mapping(target = "plateId", source = "id")
    Vehicle map(VehicleDocument doc);
    @Mapping(target = "id", source = "plateId")
    VehicleDocument map(Vehicle v);

    @Mapping(target = "email", source = "id")
    Driver map(DriverDocument doc);
    @Mapping(target = "id", source = "email")
    DriverDocument map(Driver domain);
}
