package com.exercise.transportsystem.passengers.infra.persistence.mongodb;

import com.exercise.transportsystem.domain.passengers.Passenger;
import com.exercise.transportsystem.domain.passengers.Trip;
import com.exercise.transportsystem.passengers.domain.exception.PassengersDomainException;
import com.exercise.transportsystem.passengers.domain.repository.PassengerRepository;
import com.exercise.transportsystem.passengers.infra.persistence.mongodb.mapper.PassengerMapper;
import com.exercise.transportsystem.passengers.infra.persistence.mongodb.repository.MongoPassengerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

@Repository
@Slf4j
@RequiredArgsConstructor
public class MongoPassengerRepositoryAdapter implements PassengerRepository {

    private final MongoPassengerRepository passengerRepo;
    private final PassengerMapper mapper;

    @Override
    public Flux<Passenger> findAll() {
        return passengerRepo.findAll()
                .map(mapper::map)
                .onErrorMap(t -> new PassengersDomainException(t.getMessage(), 500));
    }

    @Override
    public Mono<Passenger> findById(String id) {
        return passengerRepo.findById(id)
                .map(mapper::map)
                .onErrorMap(t -> new PassengersDomainException(t.getMessage(), 500));
    }

    @Override
    public Mono<Passenger> insert(Passenger r) {
        return passengerRepo.existsById(r.getEmail())
                .flatMap(exists -> {
                    if (exists) {
                        return Mono.error(new PassengersDomainException(String.format("%s already exists", r.getEmail()), 400));
                    }
                    return passengerRepo.insert(mapper.map(r))
                            .map(mapper::map)
                            .onErrorMap(t -> new PassengersDomainException(t.getMessage(), 500));
                });
    }

    @Override
    public Mono<Tuple2<Boolean, Passenger>> createOrUpdate(Passenger p) {
        return passengerRepo.existsById(p.getEmail())
                .flatMap(exists -> {
                    return this.passengerRepo.save(mapper.map(p))
                            .map(doc -> Tuples.of(exists, mapper.map(doc)));
                })
                .onErrorMap(t -> new PassengersDomainException(t.getMessage(), 500));
    }

    @Override
    public Mono<Void> deleteById(String id) {
        return passengerRepo.deleteById(id)
                .onErrorMap(t -> new PassengersDomainException(t.getMessage(), 500));
    }

    @Override
    public Flux<Trip> getTripsOfPassenger(String email) {
        return passengerRepo.findById(email)
                .flatMapMany(doc -> Flux.fromIterable(doc.getTrips()))
                .switchIfEmpty(Mono.error(new PassengersDomainException(String.format("%s not found", email), 404)));
    }

    @Override
    public Mono<Passenger> addTripToPassenger(String email, Trip t) {
        return passengerRepo.findById(email)
                .flatMap(doc -> {
                            doc.getTrips().add(t);
                            return passengerRepo.save(doc);
                        }
                ).map(mapper::map)
                .switchIfEmpty(Mono.error(new PassengersDomainException(String.format("%s not found", email), 404)));

    }


}
