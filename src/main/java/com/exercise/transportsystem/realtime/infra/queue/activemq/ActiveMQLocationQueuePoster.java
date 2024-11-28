package com.exercise.transportsystem.realtime.infra.queue.activemq;

import com.exercise.transportsystem.common.domain.queue.LocationQueuePoster;
import com.exercise.transportsystem.common.domain.queue.message.MessageLocationUpdate;
import com.exercise.transportsystem.common.config.ActiveMQConfig;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
@ConditionalOnBean(ActiveMQConfig.class)
public class ActiveMQLocationQueuePoster implements LocationQueuePoster {

    private final JmsTemplate jmsTemplate;
    @Value("${infra.queue.activemq.location.queue-name}")
    private String queueName;

    public void postLocationMessage(MessageLocationUpdate msg) {
        jmsTemplate.convertAndSend(this.queueName, msg);
    }

    @PostConstruct
    void initMessage() {
        log.info("{} started with {} for queue \"{}\"", this.getClass().getName(), this.jmsTemplate.getClass().getName(), this.queueName);
    }
}
