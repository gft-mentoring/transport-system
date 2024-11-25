package com.exercise.transportsystem.vehicles.infra.persistence.mongodb;

import com.exercise.transportsystem.domain.vehicle.Vehicle;
import com.exercise.transportsystem.domain.vehicle.VehicleStatus;
import com.exercise.transportsystem.vehicles.domain.exception.VehiclesDomainException;
import com.exercise.transportsystem.vehicles.domain.repository.VehicleRepository;
import com.exercise.transportsystem.vehicles.infra.persistence.mongodb.document.DriverDocument;
import com.exercise.transportsystem.vehicles.infra.persistence.mongodb.document.VehicleDocument;
import com.exercise.transportsystem.vehicles.infra.persistence.mongodb.mapper.VehicleMapper;
import com.exercise.transportsystem.vehicles.infra.persistence.mongodb.repository.MongoDriverRepository;
import com.exercise.transportsystem.vehicles.infra.persistence.mongodb.repository.MongoVehicleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import java.util.concurrent.atomic.AtomicLong;

@Repository
@RequiredArgsConstructor
@Slf4j
public class MongoVehicleRepositoryAdapter implements VehicleRepository {

    private final MongoDriverRepository driverRepo;
    private final MongoVehicleRepository vehicleRepo;
    private final VehicleMapper mapper;

    @Override
    public Mono<Vehicle> findById(String plateId) {
        return vehicleRepo.findById(plateId)
                .flatMap(this::assembleVehicleValue)
                .onErrorMap(t -> new VehiclesDomainException(t.getMessage(), 500));
    }

    @Override
    public Flux<Vehicle> findAll() {
        AtomicLong vehiclesCount = new AtomicLong(0);
        return vehicleRepo.findAll()
                .flatMap(this::assembleVehicleValue, 4)
                .doFirst(() -> vehiclesCount.set(0))
                .doOnNext(v -> {
                    log.trace("Fetched vehicle {}", v.getPlateId());
                    vehiclesCount.incrementAndGet();
                })
                .doOnSubscribe(s -> log.info("Started fetching vehicles"))
                .doOnComplete(() -> log.info("Done fetching vehicles: {}", vehiclesCount.longValue()))
                .onErrorMap(t -> new VehiclesDomainException(t.getMessage(), 500));
    }

    @Override
    public Flux<Vehicle> findAllByStatus(VehicleStatus status) {
        AtomicLong vehiclesCount = new AtomicLong(0);
        return this.findAll()
                .filter(v -> v.getStatus().equals(status))
                .doFirst(() -> vehiclesCount.set(0))
                .doOnNext(v -> vehiclesCount.incrementAndGet())
                .doOnSubscribe(s -> log.info("Filtering by status = {}", status.name()))
                .doOnComplete(() -> log.info("Count after filter: {}", vehiclesCount.longValue()))
                .onErrorMap(t -> new VehiclesDomainException(t.getMessage(), 500));
    }

    @Transactional
    @Override
    public Mono<Vehicle> insert(Vehicle v) {
        return vehicleRepo.existsById(v.getPlateId())
                .flatMap(exists -> {
                    if (exists) {
                        return Mono.error(new VehiclesDomainException(String.format("%s already exists", v.getPlateId()), 400));
                    }
                    return this.saveVehicleDocument(v)
                            .map(vDoc -> mapper.map(vDoc).setDriver(v.getDriver()))
                            .onErrorMap(t -> new VehiclesDomainException(t.getMessage(), 500));
                });
    }

    @Transactional
    @Override
    public Mono<Tuple2<Boolean, Vehicle>> createOrUpdate(Vehicle v) {
        return vehicleRepo.existsById(v.getPlateId())
                .flatMap(exists -> {
                    return this.saveVehicleDocument(v)
                            .map(vDoc -> Tuples.of(exists, mapper.map(vDoc).setDriver(v.getDriver())));
                })
                .onErrorMap(t -> new VehiclesDomainException(t.getMessage(), 500));
    }

    @Override
    @Transactional
    public Mono<Void> deleteById(String plateId) {
        return vehicleRepo.findById(plateId).flatMap(vDoc -> {
                    String driverEmail = vDoc.getDriverEmail();
                    if (driverEmail == null) {
                        return vehicleRepo.deleteById(plateId);
                    }
                    return Mono.when(this.removeVehicleReference(driverEmail), vehicleRepo.deleteById(plateId));
                }
        ).onErrorMap(t -> new VehiclesDomainException(t.getMessage(), 500));
    }

    @Override
    @Transactional
    public Mono<Void> deleteAll() {
        return Mono.when(driverRepo.deleteAll(), vehicleRepo.deleteAll());
    }

    private Mono<DriverDocument> removeVehicleReference(String driverEmail) {
        return driverRepo.findById(driverEmail).flatMap(dDoc -> {
            dDoc.setVehicleId(null);
            return driverRepo.save(dDoc);
        });
    }

    private Mono<VehicleDocument> saveVehicleDocument(Vehicle v) {
        return (v.getDriver() == null)
                ? vehicleRepo.save(mapper.map(v))
                : driverRepo.save(mapper.map(v.getDriver()).setVehicleId(v.getPlateId())).
                flatMap(driverDoc -> vehicleRepo.save(mapper.map(v).setDriverEmail(driverDoc.getId())));
    }

    private Mono<Vehicle> assembleVehicleValue(VehicleDocument document) {
        return (document.getDriverEmail() != null)
                ? driverRepo.findById(document.getDriverEmail()).map(dDoc -> mapper.map(document).setDriver(mapper.map(dDoc)))
                : Mono.just(mapper.map(document));
    }

}
