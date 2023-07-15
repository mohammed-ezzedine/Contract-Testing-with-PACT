package com.example.contracttesting.provider.store;

import com.example.contracttesting.provider.core.Spell;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SpellDataSeed {
    public static List<Spell> populate() throws IOException {
        byte[] spellsBytes = StreamUtils.copyToByteArray(SpellDataSeed.class.getClassLoader().getResourceAsStream("spells.json"));
        Spell[] spells = new ObjectMapper().readValue(spellsBytes, Spell[].class);
        return new ArrayList<>(Arrays.asList(spells));
    }
}
