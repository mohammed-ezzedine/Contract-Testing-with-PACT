package com.example.contracttesting.consumer.infra.http;

import com.example.contracttesting.consumer.config.ProviderConfig;
import com.example.contracttesting.consumer.core.Spell;
import com.example.contracttesting.consumer.core.SpellAlreadyExistsException;
import com.example.contracttesting.consumer.core.SpellReadClient;
import com.example.contracttesting.consumer.core.SpellsWriteClient;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Primary
@RequiredArgsConstructor
public class SpellClient implements SpellReadClient, SpellsWriteClient {

    private final RestTemplate restTemplate;

    @Override
    public List<Spell> getSpells() {
        ResponseEntity<Spell[]> responseEntity = restTemplate.getForEntity(ProviderConfig.allSpellsUrl(), Spell[].class);
        return List.of(Objects.requireNonNull(responseEntity.getBody()));
    }

    @Override
    public Optional<Spell> getSpell(String name) {
        try {
            ResponseEntity<Spell> responseEntity = restTemplate.getForEntity(ProviderConfig.getSpellByNameUrl(name), Spell.class);
            return Optional.ofNullable(responseEntity.getBody());
        } catch (HttpClientErrorException.NotFound e) {
            return Optional.empty();
        }
    }

    @Override
    public void addSpell(Spell spell) throws SpellAlreadyExistsException {
        try {
            restTemplate.postForEntity(ProviderConfig.addSpellUrl(), spell, Spell.class);
        } catch (HttpClientErrorException.Conflict e) {
            throw new SpellAlreadyExistsException();
        }
    }
}
