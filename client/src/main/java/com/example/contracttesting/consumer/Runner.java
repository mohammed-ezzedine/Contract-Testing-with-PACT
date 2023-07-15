package com.example.contracttesting.consumer;

import com.example.contracttesting.consumer.core.Spell;
import com.example.contracttesting.consumer.core.SpellAlreadyExistsException;
import com.example.contracttesting.consumer.core.SpellReadClient;
import com.example.contracttesting.consumer.core.SpellsWriteClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component

public class Runner implements CommandLineRunner {

    private final SpellReadClient spellReadClient;
    private final SpellsWriteClient spellWriteClient;
    private final SpellsWriteClient spellAsyncWriteClient;

    public Runner(SpellReadClient spellReadClient,
                  SpellsWriteClient spellWriteClient,
                  @Qualifier("async") SpellsWriteClient spellAsyncWriteClient) {
        this.spellReadClient = spellReadClient;
        this.spellWriteClient = spellWriteClient;
        this.spellAsyncWriteClient = spellAsyncWriteClient;
    }

    @Override
    public void run(String... args) throws InterruptedException {
        printAllSpells();

        printSpell("Accio");

        printSpell("Hokus Pokus");

        addNewSpellThroughRest("Inceptundo Infernotum", "Makes the opponent delusional for the next half an hour");

        printSpell("Inceptundo Infernotum");

        addNewSpellThroughMessaging("Invisibilis", "Makes you invisible");

        Thread.sleep(1000); // wait for the event to get processed

        printSpell("Invisibilis");
    }

    private void addNewSpellThroughRest(String spellName, String description) {
        log.info("Adding new spell {}", spellName);
        Spell spell = Spell.builder().name(spellName).description(description).build();
        try {
            spellWriteClient.addSpell(spell);
        } catch (SpellAlreadyExistsException e) {
            log.info("Failed to add a new spell");
        }
    }

    private void addNewSpellThroughMessaging(String spellName, String description) {
        log.info("Adding new spell {}", spellName);
        Spell spell = Spell.builder().name(spellName).description(description).build();
        try {
            spellAsyncWriteClient.addSpell(spell);
        } catch (SpellAlreadyExistsException e) {
            log.info("Failed to add a new spell");
        }
    }

    private void printAllSpells() {
        log.info("Fetching all spells...");
        log.info(spellReadClient.getSpells().toString());
    }

    private void printSpell(String spellName) {
        log.info("Fetching spell '{}'...", spellName);
        Optional<Spell> spell = spellReadClient.getSpell(spellName);
        spell.ifPresentOrElse(s -> log.info(s.toString()), () -> log.info("'{}' does not exist.", spellName));
    }
}
