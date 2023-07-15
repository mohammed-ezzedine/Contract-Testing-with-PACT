package com.example.contracttesting.provider.core;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SpellService implements SpellReadService, SpellWriteService {

    private final SpellFetcher spellFetcher;
    private final SpellPersister spellPersister;

    @Override
    public List<Spell> getAll() {
        return spellFetcher.getAll();
    }

    @Override
    public Spell getByName(String name) throws SpellNotFoundException {
        log.info("Fetching spell {}", name);

        if (!spellExists(name)) {
            log.error("Could not fetch spell {}, because it does not exist.", name);
            throw new SpellNotFoundException(name);
        }

        return spellFetcher.getByName(name);
    }

    private boolean spellExists(String name) {
        return spellFetcher.spellExists(name);
    }

    @Override
    public void save(Spell spell) throws SpellAlreadyExistsException {
        log.info("Saving new spell {}", spell);

        if (spellExists(spell.getName())) {
            log.error("could not add spell {}, because it already exists.", spell.getName());
            throw new SpellAlreadyExistsException(spell.getName());
        }

        spellPersister.save(spell);
    }
}
