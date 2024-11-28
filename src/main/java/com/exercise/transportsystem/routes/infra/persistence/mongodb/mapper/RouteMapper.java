package com.exercise.transportsystem.routes.infra.persistence.mongodb.mapper;

import com.exercise.transportsystem.common.domain.routes.Route;
import com.exercise.transportsystem.routes.infra.persistence.mongodb.document.RouteDocument;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface RouteMapper {

    Route map(RouteDocument doc);
    RouteDocument map(Route doc);

}
