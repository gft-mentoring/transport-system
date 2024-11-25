package com.exercise.transportsystem.passengers.infra.persistence.mongodb.repository;

import com.exercise.transportsystem.passengers.infra.persistence.mongodb.document.PassengerDocument;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface MongoPassengerRepository extends ReactiveMongoRepository<PassengerDocument, String> {
}
