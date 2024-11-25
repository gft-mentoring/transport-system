package com.exercise.transportsystem.vehicles.domain.repository;

import com.exercise.transportsystem.domain.vehicle.Vehicle;
import com.exercise.transportsystem.domain.vehicle.VehicleStatus;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

public interface VehicleRepository {
    Flux<Vehicle> findAll();
    Flux<Vehicle> findAllByStatus(VehicleStatus status);
    Mono<Vehicle> findById(String plateId);
    Mono<Vehicle> insert(Vehicle v);
    Mono<Tuple2<Boolean, Vehicle>> createOrUpdate(Vehicle v);
    Mono<Void> deleteById(String plateId);
    Mono<Void> deleteAll();
}
