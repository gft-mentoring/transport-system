package com.exercise.transportsystem.passengers.infra.persistence.mongodb.mapper;

import com.exercise.transportsystem.common.domain.passengers.Passenger;
import com.exercise.transportsystem.passengers.infra.persistence.mongodb.document.PassengerDocument;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PassengerMapper {

    @Mapping(target = "email", source = "id")
    Passenger map(PassengerDocument doc);
    @Mapping(target = "id", source = "email")
    PassengerDocument map(Passenger p);

}
