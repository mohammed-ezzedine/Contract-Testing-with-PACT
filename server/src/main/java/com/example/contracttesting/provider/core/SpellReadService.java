package com.example.contracttesting.provider.core;

import java.util.List;

public interface SpellReadService {
    List<Spell> getAll();
    Spell getByName(String name) throws SpellNotFoundException;
}
