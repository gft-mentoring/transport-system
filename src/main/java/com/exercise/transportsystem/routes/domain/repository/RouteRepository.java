package com.exercise.transportsystem.routes.domain.repository;

import com.exercise.transportsystem.common.domain.routes.Route;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

public interface RouteRepository {

    Flux<Route> findAll();
    Mono<Route> findById(String id);
    Mono<Route> insert(Route r);
    Mono<Tuple2<Boolean, Route>> createOrUpdate(Route r);
    Mono<Void> deleteById(String id);
}
