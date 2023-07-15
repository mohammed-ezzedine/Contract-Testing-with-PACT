package com.example.contracttesting.provider.core;

public class SpellAlreadyExistsException extends Exception {
    public SpellAlreadyExistsException(String name) {
        super(String.format("Spell '%s' already exists.", name));
    }
}
