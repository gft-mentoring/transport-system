package com.exercise.transportsystem.routes.infra.persistence.mongodb.repository;

import com.exercise.transportsystem.routes.infra.persistence.mongodb.document.RouteDocument;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface MongoRouteRepository extends ReactiveMongoRepository<RouteDocument, String> {
}
