package com.example.contracttesting.provider.core;

public interface SpellWriteService {
    void save(Spell spell) throws SpellAlreadyExistsException;
}
