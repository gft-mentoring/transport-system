package com.exercise.transportsystem.passengers.domain.repository;

import com.exercise.transportsystem.common.domain.passengers.Passenger;
import com.exercise.transportsystem.common.domain.passengers.Trip;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

public interface PassengerRepository {

    Flux<Passenger> findAll();
    Mono<Passenger> findById(String email);
    Mono<Passenger> insert(Passenger r);
    Mono<Tuple2<Boolean, Passenger>> createOrUpdate(Passenger r);
    Mono<Void> deleteById(String email);
    Mono<Passenger> addTripToPassenger(String email, Trip t);
    Flux<Trip> getTripsOfPassenger(String email);

}
