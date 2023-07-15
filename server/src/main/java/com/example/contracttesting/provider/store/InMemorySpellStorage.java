package com.example.contracttesting.provider.store;

import com.example.contracttesting.provider.core.Spell;
import com.example.contracttesting.provider.core.SpellFetcher;
import com.example.contracttesting.provider.core.SpellPersister;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class InMemorySpellStorage implements SpellFetcher, SpellPersister {

    private final List<Spell> spells;

    public InMemorySpellStorage() throws IOException {
        spells = SpellDataSeed.populate();
    }

    @Override
    public List<Spell> getAll() {
        return spells;
    }

    @Override
    public boolean spellExists(String name) {
        return spells.stream().anyMatch(spell -> spell.getName().equalsIgnoreCase(name));
    }

    @Override
    public Spell getByName(String name) {
        return spells.stream().filter(spell -> spell.getName().equalsIgnoreCase(name)).findFirst().get();
    }

    @Override
    public void save(Spell spell) {
        spells.add(spell);
    }
}
