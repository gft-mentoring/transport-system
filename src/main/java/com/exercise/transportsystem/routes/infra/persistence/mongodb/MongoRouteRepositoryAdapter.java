package com.exercise.transportsystem.routes.infra.persistence.mongodb;

import com.exercise.transportsystem.domain.routes.Route;
import com.exercise.transportsystem.routes.domain.exception.RoutesDomainException;
import com.exercise.transportsystem.routes.domain.repository.RouteRepository;
import com.exercise.transportsystem.routes.infra.persistence.mongodb.mapper.RouteMapper;
import com.exercise.transportsystem.routes.infra.persistence.mongodb.repository.MongoRouteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

@Repository
@RequiredArgsConstructor
@Slf4j
public class MongoRouteRepositoryAdapter implements RouteRepository {

    private final RouteMapper mapper;
    private final MongoRouteRepository routeRepo;

    @Override
    public Flux<Route> findAll() {
        return routeRepo.findAll()
                .map(mapper::map)
                .onErrorMap(t -> new RoutesDomainException(t.getMessage(), 500));
    }

    @Override
    public Mono<Route> findById(String id) {
        return routeRepo.findById(id)
                .map(mapper::map)
                .onErrorMap(t -> new RoutesDomainException(t.getMessage(), 500));
    }

    @Override
    public Mono<Route> insert(Route r) {
        return routeRepo.existsById(r.getId().toString())
                .flatMap(exists -> {
                    if (exists) {
                        return Mono.error(new RoutesDomainException(String.format("%s already exists", r.getId()), 400));
                    }
                    return routeRepo.insert(mapper.map(r))
                            .map(mapper::map)
                            .onErrorMap(t -> new RoutesDomainException(t.getMessage(), 500));
                });
    }

    @Override
    public Mono<Tuple2<Boolean, Route>> createOrUpdate(Route r) {
        return routeRepo.existsById(r.getId().toString())
                .flatMap(exists -> {
                    return this.routeRepo.save(mapper.map(r))
                            .map(doc -> Tuples.of(exists, mapper.map(doc)));
                })
                .onErrorMap(t -> new RoutesDomainException(t.getMessage(), 500));
    }

    @Override
    public Mono<Void> deleteById(String id) {
        return routeRepo.deleteById(id)
                .onErrorMap(t -> new RoutesDomainException(t.getMessage(), 500));
    }


}
