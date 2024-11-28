package com.exercise.transportsystem.vehicles.api.rest;

import com.exercise.transportsystem.common.domain.vehicle.Vehicle;
import com.exercise.transportsystem.common.domain.vehicle.VehicleStatus;
import com.exercise.transportsystem.vehicles.domain.exception.VehiclesDomainException;
import com.exercise.transportsystem.vehicles.domain.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/vehicles")
@RequiredArgsConstructor
@Slf4j
public class RestVehicleController {

    private final VehicleRepository vehicleRepository;

    @GetMapping({"", "/"})
    public Flux<Vehicle> findAll(@RequestParam(required = false) VehicleStatus status) {
        var flux = (status == null) ? vehicleRepository.findAll() : vehicleRepository.findAllByStatus(status);
        return flux;
    }

    @GetMapping({"/{plateId}"})
    public Mono<Vehicle> findById(@PathVariable String plateId) {
        return vehicleRepository.findById(plateId)
                .switchIfEmpty(Mono.error(new VehiclesDomainException(String.format("Vehicle %s not found", plateId), 404)));
    }

    @PostMapping({"", "/"})
    public Mono<ResponseEntity<Vehicle>> addVehicle(@RequestBody Vehicle vehicle) {

        return vehicleRepository.insert(vehicle)
                .doFirst(() -> log.info("{}", vehicle))
                .map(v -> new ResponseEntity<>(v, HttpStatus.CREATED));
    }

    @PutMapping({"", "/"})
    public Mono<ResponseEntity<Vehicle>> updateVehicle(@RequestBody Vehicle vehicle) {
        return vehicleRepository.createOrUpdate(vehicle)
                .map(tups -> new ResponseEntity<>(tups.getT2(), tups.getT1() ? HttpStatus.OK : HttpStatus.CREATED));
    }

    @DeleteMapping("/{plateId}")
    public Mono<ResponseEntity<Void>> deleteVehicle(@PathVariable String plateId) {
        return vehicleRepository.deleteById(plateId)
                .map(v -> new ResponseEntity<>(v, HttpStatus.valueOf(204)));
    }


}
