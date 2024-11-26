package com.exercise.transportsystem.vehicles.infra.queue.activemq;

import com.exercise.transportsystem.domain.message.MessageLocationUpdate;
import com.exercise.transportsystem.vehicles.domain.messages.VehicleMessages;
import com.exercise.transportsystem.vehicles.domain.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Component
@Slf4j
@RequiredArgsConstructor
public class ActiveMQVehicleReceiver implements VehicleMessages {

    private final VehicleRepository vehicleRepository;

    @JmsListener(destination = "location", containerFactory = "exampleFactory")
    public void receiveLocationUpdate(MessageLocationUpdate locMsg) {
        Mono.defer(() -> vehicleRepository.findById(locMsg.getVehicleId())
                .flatMap(vehicle -> {
                    vehicle.setCoordinates(locMsg.getCoordinates());
                    return vehicleRepository.createOrUpdate(vehicle);
                }))
                .subscribeOn(Schedulers.boundedElastic())
                .switchIfEmpty(Mono.fromRunnable(() -> log.warn("{} does not exist", locMsg.getVehicleId())))
                .doOnSubscribe((s) -> log.info("Received message: " + locMsg))
                .subscribe();

    }
}
