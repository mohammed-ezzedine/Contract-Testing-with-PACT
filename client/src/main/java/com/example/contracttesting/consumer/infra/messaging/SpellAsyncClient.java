package com.example.contracttesting.consumer.infra.messaging;

import com.example.contracttesting.consumer.core.Spell;
import com.example.contracttesting.consumer.core.SpellsWriteClient;
import com.example.contracttesting.consumer.infra.messaging.event.NewSpellRequestedEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Qualifier("async")
@Service
@RequiredArgsConstructor
public class SpellAsyncClient implements SpellsWriteClient {

    private final RabbitTemplate rabbitTemplate;
    private final RabbitProperties rabbitProperties;

    @SneakyThrows
    @Override
    public void addSpell(Spell spell) {
        NewSpellRequestedEvent event = NewSpellRequestedEvent.builder().name(spell.getName()).description(spell.getDescription()).build();
        rabbitTemplate.convertAndSend(rabbitProperties.getAddSpellExchange(), rabbitProperties.getAddSpellRoutingKey(), new ObjectMapper().writeValueAsBytes(event));
    }
}
