package com.fitness.aiservice.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

    @Value("${rabbitmq.exchange.name}") // gets the name from yml file
    private String exchange;
    @Value("${rabbitmq.queue.name}") // gets the name from yml file
    private String queue;
    @Value("${rabbitmq.routing.key}")
    private String routingKey;

    // Declares a queue name activity.queue in RabbitMQ
    // Param durable means that if RabbitMQ restarts, the queue will still remain with messages
    // AMQP concept: queue is a buffer that holds messages
    @Bean
    public Queue activityQueue() {
        return new Queue(queue, true);
    }

    // Routes messages based on routing key match, and exchange decided which queue gets the message
    @Bean
    public DirectExchange activityExchange() {
//        return new DirectExchange("fitness.exchange");
        return new DirectExchange(exchange);
    }

    // Spring injects the queue and exchange beans, and connects exchange to queue
    // Messages with routing key go to queue
    @Bean
    public Binding activityBinding(Queue activityQueue, DirectExchange activityExchange) {
//        return BindingBuilder.bind(activityQueue).to(activityExchange).with("activity.tracking");
        return BindingBuilder.bind(activityQueue).to(activityExchange).with(routingKey);
    }

    // Converts Java objects into JSON before sending them to RabbitMQ
    // Without this method, RabbitMQ sends messages as raw byte[]
    // And without the converter, we have to manually serialize/deserialize the message
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
