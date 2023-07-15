package com.example.contracttesting.provider.messaging;

import com.example.contracttesting.provider.core.Spell;
import com.example.contracttesting.provider.core.SpellAlreadyExistsException;
import com.example.contracttesting.provider.core.SpellWriteService;
import com.example.contracttesting.provider.messaging.event.NewSpellRequestedEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class SpellsRabbitHandler {

    private final SpellWriteService spellWriteService;

    @RabbitListener(queues = "${messaging.incoming.add-spells.queue}")
    void handleGetAllSpellsCommand(Message message) throws IOException {
        log.info("Received a request to add a new spell {}", message);
        try {
            NewSpellRequestedEvent command = new ObjectMapper().readValue(message.getBody(), NewSpellRequestedEvent.class);
            Spell spell = Spell.builder().name(command.getName()).description(command.getDescription()).build();
            spellWriteService.save(spell);
        } catch (SpellAlreadyExistsException e) {
            log.error("Failed to add a new spell because it already existed.");
        }
    }
}
