package com.exercise.transportsystem.vehicles.domain.messages;

import com.exercise.transportsystem.domain.message.MessageLocationUpdate;

public interface VehicleMessages {
    void receiveLocationUpdate(MessageLocationUpdate locMsg);
}
