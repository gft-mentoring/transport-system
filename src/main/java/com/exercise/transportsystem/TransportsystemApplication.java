package com.exercise.transportsystem;

import com.exercise.transportsystem.domain.Coordinates;
import com.exercise.transportsystem.domain.message.MessageLocationUpdate;
import com.exercise.transportsystem.realtime.infra.queue.activemq.ActiveMQLocationMessageSender;
import jakarta.jms.ConnectionFactory;
import org.jeasy.random.EasyRandom;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jms.DefaultJmsListenerContainerFactoryConfigurer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;

@SpringBootApplication
@EnableReactiveMongoRepositories
@EnableJms
public class TransportsystemApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(TransportsystemApplication.class, args);
    }

    @Bean
    public JmsListenerContainerFactory<?> exampleFactory(ConnectionFactory connectionFactory, DefaultJmsListenerContainerFactoryConfigurer configurer) {
        var factory = new DefaultJmsListenerContainerFactory();
        configurer.configure(factory, connectionFactory);
        return factory;
    }

}
