package com.example.contracttesting.provider.core;

public class SpellNotFoundException extends Exception {
    public SpellNotFoundException(String name) {
        super(String.format("Spell '%s' does not exist.", name));
    }
}
