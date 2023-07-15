package com.example.contracttesting.consumer.infra.messaging;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
public class RabbitProperties {

    @Value("${messaging.outgoing.add-spells.exchange}")
    private String addSpellExchange;

    @Value("${messaging.outgoing.add-spells.routing-key}")
    private String addSpellRoutingKey;
}
