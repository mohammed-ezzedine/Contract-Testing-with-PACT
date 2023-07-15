package com.example.contracttesting.consumer.core;

public interface SpellsWriteClient {
    void addSpell(Spell spell) throws SpellAlreadyExistsException;
}
