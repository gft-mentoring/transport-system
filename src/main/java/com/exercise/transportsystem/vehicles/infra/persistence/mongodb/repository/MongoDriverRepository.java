package com.exercise.transportsystem.vehicles.infra.persistence.mongodb.repository;

import com.exercise.transportsystem.vehicles.infra.persistence.mongodb.document.DriverDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface MongoDriverRepository extends ReactiveMongoRepository<DriverDocument, String> {
}
