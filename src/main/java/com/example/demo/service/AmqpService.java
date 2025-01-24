package com.example.demo.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@PropertySource("classpath:other.properties")
public class AmqpService implements IAmqp {

    @Value("${amqp.queue}")
    public String QUEUE_NAME;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    public void generateMessage(String message) {
        log.info("Consume service generateMessage");
        rabbitTemplate.convertAndSend(QUEUE_NAME, message);
    }
}
