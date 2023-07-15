package com.example.contracttesting.provider.core;

import java.util.List;

public interface SpellFetcher {
    List<Spell> getAll();
    boolean spellExists(String name);
    Spell getByName(String name);
}
