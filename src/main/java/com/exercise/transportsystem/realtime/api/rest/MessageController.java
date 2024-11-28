package com.exercise.transportsystem.realtime.api.rest;

import com.exercise.transportsystem.common.domain.queue.LocationQueuePoster;
import com.exercise.transportsystem.common.domain.queue.message.MessageLocationUpdate;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController {

    private final LocationQueuePoster locationQueuePoster;

    @PostMapping({"/locations"})
    void postLocationUpdate(@RequestBody MessageLocationUpdate msg) {
        locationQueuePoster.postLocationMessage(msg);
    }

}
