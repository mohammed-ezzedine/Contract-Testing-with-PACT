package com.example.contracttesting.provider.messaging;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class RabbitMQConfig {

    private final RabbitProperties rabbitProperties;

    @Bean
    Queue queue(AmqpAdmin amqpAdmin) {
        Queue queue = new Queue(rabbitProperties.getGetAllSpellsCommandQueue(), false);
        amqpAdmin.declareQueue(queue);
        return queue;
    }

    @Bean
    Exchange exchange(AmqpAdmin amqpAdmin) {
        Exchange exchange = ExchangeBuilder.directExchange(rabbitProperties.getGetAllSpellsCommandExchange()).durable(true).build();

        amqpAdmin.declareExchange(exchange);

        return exchange;
    }

    @Bean
    Binding getAllSpellsBinding(Queue queue, Exchange exchange, AmqpAdmin amqpAdmin) {
        Binding binding = BindingBuilder.bind(queue).to(exchange).with(rabbitProperties.getGetAllSpellsCommandRoutingKey()).noargs();
        amqpAdmin.declareBinding(binding);
        return binding;
    }
}
