package com.exercise.transportsystem.passengers.api.rest;

import com.exercise.transportsystem.domain.passengers.Passenger;
import com.exercise.transportsystem.domain.passengers.Trip;
import com.exercise.transportsystem.passengers.domain.exception.PassengersDomainException;
import com.exercise.transportsystem.passengers.domain.repository.PassengerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("api/passengers")
@RequiredArgsConstructor
@Slf4j
public class PassengerController {

    private final PassengerRepository passengerRepository;

    @GetMapping({"", "/"})
    public Flux<Passenger> findAll() {
        return passengerRepository.findAll();
    }

    @GetMapping({"/{email}"})
    public Mono<Passenger> findById(@PathVariable String email) {
        return passengerRepository.findById(email)
                .switchIfEmpty(Mono.error(new PassengersDomainException(String.format("Passenger %s not found", email), 404)));
    }

    @PostMapping({"", "/"})
    public Mono<ResponseEntity<Passenger>> addPassenger(@RequestBody Passenger vehicle) {

        return passengerRepository.insert(vehicle)
                .doFirst(() -> log.info("{}", vehicle))
                .map(v -> new ResponseEntity<>(v, HttpStatus.CREATED));
    }

    @PutMapping({"", "/"})
    public Mono<ResponseEntity<Passenger>> updatePassenger(@RequestBody Passenger vehicle) {
        return passengerRepository.createOrUpdate(vehicle)
                .map(tups -> new ResponseEntity<>(tups.getT2(), tups.getT1() ? HttpStatus.OK : HttpStatus.CREATED));
    }

    @DeleteMapping("/{email}")
    public Mono<ResponseEntity<Void>> deletePassenger(@PathVariable String email) {
        return passengerRepository.deleteById(email)
                .map(v -> new ResponseEntity<>(v, HttpStatus.valueOf(204)));
    }

    @GetMapping({"/{email}/trips", "/{email}/trips/"})
    public Flux<Trip> getTripsOfPassenger(@PathVariable String email, Trip trip) {
        return passengerRepository.getTripsOfPassenger(email);
    }
    @PostMapping({"/{email}/trips", "/{email}/trips/"})
    public Mono<Passenger> addTripToPassenger(@PathVariable String email, Trip trip) {
        return passengerRepository.addTripToPassenger(email, trip);
    }
}
