package com.exercise.transportsystem.realtime.api.rest;

import com.exercise.transportsystem.domain.message.MessageLocationUpdate;
import com.exercise.transportsystem.realtime.infra.queue.activemq.ActiveMQLocationMessageSender;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController {

    private final ActiveMQLocationMessageSender locationMessageSender;

    @PostMapping({"/locations"})
    void postLocationUpdate(@RequestBody MessageLocationUpdate msg) { // TODO validations
        locationMessageSender.postLocationMessage(msg);
    }

}
