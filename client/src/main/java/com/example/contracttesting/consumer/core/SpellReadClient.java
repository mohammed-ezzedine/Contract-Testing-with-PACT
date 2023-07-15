package com.example.contracttesting.consumer.core;

import java.util.List;
import java.util.Optional;

public interface SpellReadClient {
    List<Spell> getSpells();
    Optional<Spell> getSpell(String name);
}
