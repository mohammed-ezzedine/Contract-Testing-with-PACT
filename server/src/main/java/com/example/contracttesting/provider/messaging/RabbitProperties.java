package com.example.contracttesting.provider.messaging;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
public class RabbitProperties {

    @Value("${messaging.incoming.add-spells.queue}")
    private String getAllSpellsCommandQueue;

    @Value("${messaging.incoming.add-spells.routing-key}")
    private String getAllSpellsCommandRoutingKey;

    @Value("${messaging.incoming.add-spells.exchange}")
    private String getAllSpellsCommandExchange;
}
