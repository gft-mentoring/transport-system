package com.exercise.transportsystem.vehicles.application.queue;

import com.exercise.transportsystem.common.domain.queue.LocationQueueListener;
import com.exercise.transportsystem.common.domain.queue.message.MessageLocationUpdate;
import com.exercise.transportsystem.common.domain.vehicle.Vehicle;
import com.exercise.transportsystem.vehicles.domain.repository.VehicleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.util.function.Function;
import java.util.function.Supplier;

@Slf4j
@Component
public class VehicleQueueManagerImpl {

    private final LocationQueueListener listener;
    private final VehicleRepository vehicleRepository;

    public VehicleQueueManagerImpl(LocationQueueListener listener, VehicleRepository vehicleRepository) {

        this.vehicleRepository = vehicleRepository;
        this.listener = listener;

        Function<MessageLocationUpdate, Supplier<Mono<Vehicle>>> cbFactory = (msg) -> {
            return () -> this.vehicleRepository.findById(msg.getVehicleId())
                    .flatMap(vehicle -> {
                        vehicle.setCoordinates(msg.getCoordinates());
                        return vehicleRepository.createOrUpdate(vehicle).map(Tuple2::getT2);
                    });
        };
        this.listener.onMessageReceived(cbFactory);
        log.info("VehicleQueueManager initialized");
    }

}
