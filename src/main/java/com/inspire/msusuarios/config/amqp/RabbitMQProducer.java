package com.inspire.msusuarios.config.amqp;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class RabbitMQProducer {

    private final AmqpTemplate amqpTemplate;

    @Value("${spring.rabbitmq.exchange}")
    private String exchange;

    @Autowired
    public RabbitMQProducer(AmqpTemplate amqpTemplate) {
        this.amqpTemplate = amqpTemplate;
    }

    public void sendToRabbit(String routingKey, Object object) {
        log.info("Sending message to RabbitMQ: {}", object);
        amqpTemplate.convertAndSend(exchange, routingKey, object);
    }

}
