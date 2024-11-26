package com.exercise.transportsystem.vehicles.infra.queue.activemq;

import com.exercise.transportsystem.common.config.ActiveMQConfig;
import com.exercise.transportsystem.common.domain.queue.message.MessageLocationUpdate;
import com.exercise.transportsystem.common.domain.vehicle.Vehicle;
import com.exercise.transportsystem.common.domain.queue.LocationQueueListener;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.function.Function;
import java.util.function.Supplier;

@Component
@Slf4j
@Setter
@RequiredArgsConstructor
@ConditionalOnBean(ActiveMQConfig.class)
public class ActiveMQLocationQueueListener implements LocationQueueListener {

    private Function<MessageLocationUpdate, Supplier<Mono<Vehicle>>> callbackFactory;

    public void onMessageReceived(Function<MessageLocationUpdate, Supplier<Mono<Vehicle>>> callback) {
        log.info("Callback {} set for {}", callback, this.getClass().getName());
        this.setCallbackFactory(callback);
    }

    @JmsListener(destination = "${infra.queue.activemq.location.queue-name}", containerFactory = "locationFactory")
    private void listener(MessageLocationUpdate locMsg) {
        if (callbackFactory == null) {
            String msg = "%s not properly initialized. A callback function must be set up".formatted(this.getClass().getName());
            log.error(msg);
            throw new IllegalStateException(msg);
        }
        var supplier = callbackFactory.apply(locMsg);
        Mono.defer(supplier)
                .subscribeOn(Schedulers.boundedElastic())
                .switchIfEmpty(Mono.fromRunnable(() -> log.warn("{} does not exist", locMsg.getVehicleId())))
                .doOnSubscribe((s) -> log.info("Received message: " + locMsg))
                .subscribe();
    }
}
