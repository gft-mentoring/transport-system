package com.exercise.transportsystem.realtime.infra.queue.activemq;

import com.exercise.transportsystem.domain.message.MessageLocationUpdate;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ActiveMQLocationMessageSender {

    private final JmsTemplate jmsTemplate;

    public void postLocationMessage(MessageLocationUpdate msg) {
        jmsTemplate.convertAndSend("location", msg);
    }

    @PostConstruct
    void initMessage() {
        log.info("{} started with {}", this.getClass().getName(), this.jmsTemplate.getClass().getName());
    }
}
