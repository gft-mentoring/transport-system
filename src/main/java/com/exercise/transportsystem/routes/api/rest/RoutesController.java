package com.exercise.transportsystem.routes.api.rest;

import com.exercise.transportsystem.common.domain.routes.Route;
import com.exercise.transportsystem.routes.domain.exception.RoutesDomainException;
import com.exercise.transportsystem.routes.domain.repository.RouteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/routes")
@RequiredArgsConstructor
@Slf4j
public class RoutesController {

    private final RouteRepository routeRepository;

    @GetMapping({"", "/"})
    public Flux<Route> findAll() {
        return routeRepository.findAll();
    }

    @GetMapping({"/{id}"})
    public Mono<Route> findById(@PathVariable String id) {
        return routeRepository.findById(id)
                .switchIfEmpty(Mono.error(new RoutesDomainException(String.format("Route %s not found", id), 404)));
    }

    @PostMapping({"", "/"})
    public Mono<ResponseEntity<Route>> addRoute(@RequestBody Route vehicle) {

        return routeRepository.insert(vehicle)
                .doFirst(() -> log.info("{}", vehicle))
                .map(v -> new ResponseEntity<>(v, HttpStatus.CREATED));
    }

    @PutMapping({"", "/"})
    public Mono<ResponseEntity<Route>> updateRoute(@RequestBody Route vehicle) {
        return routeRepository.createOrUpdate(vehicle)
                .map(tups -> new ResponseEntity<>(tups.getT2(), tups.getT1() ? HttpStatus.OK : HttpStatus.CREATED));
    }

    @DeleteMapping("/{plateId}")
    public Mono<ResponseEntity<Void>> deleteRoute(@PathVariable String plateId) {
        return routeRepository.deleteById(plateId)
                .map(v -> new ResponseEntity<>(v, HttpStatus.valueOf(204)));
    }
}
