package com.exercise.transportsystem.develop;

import com.exercise.transportsystem.domain.passengers.Passenger;
import com.exercise.transportsystem.domain.routes.Route;
import com.exercise.transportsystem.domain.vehicle.Driver;
import com.exercise.transportsystem.domain.vehicle.Vehicle;
import com.exercise.transportsystem.passengers.domain.repository.PassengerRepository;
import com.exercise.transportsystem.routes.domain.repository.RouteRepository;
import com.exercise.transportsystem.vehicles.domain.repository.VehicleRepository;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;

@Component
@Slf4j
@ConditionalOnProperty(name = "transport.data.insertOnInit", havingValue = "true", matchIfMissing = false)
@Profile({"local", "it"})
public class DataPopulator {

    private final VehicleRepository vehicleRepository;
    private final RouteRepository routeRepository;
    private final PassengerRepository passengerRepository;
    private long seed;
    private final EasyRandom rng;
    private final Environment env;

    public DataPopulator(VehicleRepository vehicleRepository,
                         RouteRepository routeRepository,
                         PassengerRepository passengerRepository,
                         @Value("${transport.data.seed:0}") long seed, @Autowired Environment env) {
        var params = new EasyRandomParameters();
        if (seed == 0L) {
            log.info("No seed configured: using current instant");
            params.setSeed(Instant.now().toEpochMilli());
        } else {
            params.setSeed(seed);
        }

        this.rng = new EasyRandom(params);
        this.vehicleRepository = vehicleRepository;
        this.routeRepository = routeRepository;
        this.passengerRepository = passengerRepository;
        this.env = env;

    }

    Vehicle randomVehicle() {
        var v = rng.nextObject(Vehicle.class);
        var d = rng.nextObject(Driver.class);
        d.setEmail(this.randomEmail());
        v.setDriver(rng.nextFloat() > 0.15 ? d : null);
        return v;
    }

    private String randomEmail() {
        return String.format("%.8s@%.5s.%.3s",
                rng.nextObject(String.class).toLowerCase(),
                rng.nextObject(String.class).toLowerCase(),
                rng.nextObject(String.class).toLowerCase());
    }

    public Mono<Void> cleanUpDatabase() {
        return vehicleRepository.deleteAll().doOnSubscribe(s -> log.warn("Dropping all vehicle records from database"));
    }

    private Flux<Vehicle> insertFluxOfVehicles(Flux<Vehicle> fluxVehicles) {
        return fluxVehicles.flatMap(vehicleRepository::createOrUpdate, 4).map(Tuple2::getT2);
    }

    public Flux<Vehicle> insertRandomVehicles(int count) {
        var randomVehicles = Flux.range(0, count).map(i -> this.randomVehicle());
        return this.insertFluxOfVehicles(randomVehicles)
                .doOnNext(v -> log.debug("Inserted vehicle {}", v));
    }

    public Flux<Vehicle> insertLisOfVehicles(List<Vehicle> listVehicles) {
        return this.insertFluxOfVehicles(Flux.fromIterable(listVehicles));
    }

    public Route randomRoute() {
        return rng.nextObject(Route.class);
    }

    private Flux<Route> insertFluxOfRoutes(Flux<Route> fluxRoutes) {
        return fluxRoutes.flatMap(routeRepository::createOrUpdate, 4).map(Tuple2::getT2);
    }

    public Flux<Route> insertRandomRoutes(int count) {
        var randomRoutes = Flux.range(0, count).map(i -> this.randomRoute());
        return this.insertFluxOfRoutes(randomRoutes)
                .doOnNext(r -> log.debug("Inserted route {}", r));
    }

    public Passenger randomPassenger() {
        return rng.nextObject(Passenger.class).setEmail(this.randomEmail());
    }

    public Flux<Passenger> insertFluxOfPassengers(Flux<Passenger> passengerFlux) {
        return passengerFlux.flatMap(passengerRepository::createOrUpdate, 4).map(Tuple2::getT2);
    }

    public Flux<Passenger> insertRandomPassengers(int count) {
        var randomPassengers = Flux.range(0, count).map(i -> this.randomPassenger());
        return this.insertFluxOfPassengers(randomPassengers)
                .doOnNext(r -> log.debug("Inserted passenger {}", r));
    }

    @PostConstruct
    void initializeData() {
        if (Arrays.asList(env.getActiveProfiles()).contains("local")) {
            var monoAll = Mono.when(
                    this.insertRandomVehicles(25),
                    this.insertRandomRoutes(10),
                    this.insertRandomPassengers(20)
            ).doFirst(() -> log.info("Populating the database for local development"));
            monoAll.block();
        }
    }
}
