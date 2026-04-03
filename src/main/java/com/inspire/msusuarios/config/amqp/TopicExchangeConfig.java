package com.inspire.msusuarios.config.amqp;

import jakarta.annotation.PostConstruct;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TopicExchangeConfig {

    private final AmqpAdmin amqpAdmin;

    @Value("${spring.rabbitmq.exchange}")
    String topicExchangeName;
    @Value("${spring.rabbitmq.routing-key}")
    String userRoutingKey;
    @Value("${spring.rabbitmq.user-queue}")
    String userQueue;


    @Autowired
    public TopicExchangeConfig(AmqpAdmin amqpAdmin) {
        this.amqpAdmin = amqpAdmin;
    }

    TopicExchange topicExchange() {
        return new TopicExchange(topicExchangeName, true, false);
    }

    Queue queue() {
        return new Queue(userQueue, true, false, false);
    }


    Binding bindingDirect() {
        return BindingBuilder.bind(queue()).to(topicExchange()).with(userRoutingKey);
    }


    @Bean
    public AmqpTemplate amqpTemplate(ConnectionFactory connectionFactory, MessageConverter jsonMessageConverter) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter);
        rabbitTemplate.setExchange(topicExchangeName);
        return rabbitTemplate;
    }

    @PostConstruct
    public void init() {
        amqpAdmin.declareQueue(queue());
        amqpAdmin.declareExchange(topicExchange());
        amqpAdmin.declareBinding(bindingDirect());
    }
}