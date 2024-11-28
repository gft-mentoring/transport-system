package com.exercise.transportsystem.common.domain.queue;

import com.exercise.transportsystem.common.domain.queue.message.MessageLocationUpdate;
import com.exercise.transportsystem.common.domain.vehicle.Vehicle;
import reactor.core.publisher.Mono;

import java.util.function.Function;
import java.util.function.Supplier;

public interface LocationQueueListener {

    /* Callback function to be called on receiving a MessageLocationUpdate */
    void onMessageReceived(Function<MessageLocationUpdate, Supplier<Mono<Vehicle>>> callback);
}
