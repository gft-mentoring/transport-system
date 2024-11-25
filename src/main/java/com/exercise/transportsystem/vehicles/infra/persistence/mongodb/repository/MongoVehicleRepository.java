package com.exercise.transportsystem.vehicles.infra.persistence.mongodb.repository;

import com.exercise.transportsystem.vehicles.infra.persistence.mongodb.document.VehicleDocument;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface MongoVehicleRepository extends ReactiveMongoRepository<VehicleDocument, String> {
//    Flux<VehicleDocument> findAllById(String plateId);
//    Flux<VehicleDocument> findAllByStatus(VehicleStatus status);
}
