package com.example.contracttesting.consumer.infra.http;

import au.com.dius.pact.consumer.MockServer;
import au.com.dius.pact.consumer.dsl.PactDslJsonArray;
import au.com.dius.pact.consumer.dsl.PactDslJsonBody;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.core.model.V4Pact;
import au.com.dius.pact.core.model.annotations.Pact;
import com.example.contracttesting.consumer.core.Spell;
import com.example.contracttesting.consumer.core.SpellAlreadyExistsException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(PactConsumerTestExt.class)
class SpellRestClientConsumerContractTest {

    public static final String CONSUMER = "spell-client";
    public static final String PROVIDER = "spell-server";

    @Pact(consumer = CONSUMER, provider = PROVIDER)
    @SuppressWarnings("unused")
    V4Pact getAllSpellsContract(PactDslWithProvider builder) {
        return builder
                .given("spells exist")
                .uponReceiving("a request to fetch all spells")
                    .path("/spells")
                    .method("GET")
                .willRespondWith()
                    .status(200)
                    .body(PactDslJsonArray.arrayEachLike()
                            .stringType("name")
                            .stringType("description")
                    )
                .toPact(V4Pact.class);
    }

    @Test
    @DisplayName("validate contract for getting all spells")
    @PactTestFor(pactMethod = "getAllSpellsContract")
    void validate_contract_for_getting_all_spells(MockServer mockServer) {
        RestTemplate restTemplate = new RestTemplateBuilder().rootUri(mockServer.getUrl()).build();
        SpellClient spellRestClient = new SpellClient(restTemplate);
        List<Spell> spells = spellRestClient.getSpells();
        assertNotEquals(0, spells.size());
    }

    @Pact(consumer = CONSUMER, provider = PROVIDER)
    @SuppressWarnings("unused")
    V4Pact getExistingSpellByNameContract(PactDslWithProvider builder) {
        return builder
                .given("spell exists", Map.of("name", "accio"))
                .uponReceiving("a request to fetch the spell")
                    .path("/spells/search")
                    .query("name=accio")
                    .method("GET")
                .willRespondWith()
                    .status(200)
                    .body(new PactDslJsonBody()
                            .stringValue("name", "accio")
                            .stringType("description")
                    )
                .toPact(V4Pact.class);
    }

    @Test
    @DisplayName("validate contract for getting an existing spell")
    @PactTestFor(pactMethod = "getExistingSpellByNameContract")
    void validate_contract_for_getting_an_existing_spell(MockServer mockServer) {
        RestTemplate restTemplate = new RestTemplateBuilder().rootUri(mockServer.getUrl()).build();
        SpellClient spellRestClient = new SpellClient(restTemplate);
        Optional<Spell> spell = spellRestClient.getSpell("accio");
        assertTrue(spell.isPresent());
    }

    @Pact(consumer = CONSUMER, provider = PROVIDER)
    @SuppressWarnings("unused")
    V4Pact getNonExistingSpellContract(PactDslWithProvider builder) {
        return builder
                .given("spell does not exist", Map.of("name", "hokus pocus"))
                .uponReceiving("a request to fetch the spell")
                    .path("/spells/search")
                    .query("name=hokus pocus")
                    .method("GET")
                .willRespondWith()
                    .status(404)
                .toPact(V4Pact.class);
    }

    @Test
    @DisplayName("validate contract for getting a non existing spell")
    @PactTestFor(pactMethod = "getNonExistingSpellContract")
    void validate_contract_for_getting_a_non_existing_spell(MockServer mockServer) {
        RestTemplate restTemplate = new RestTemplateBuilder().rootUri(mockServer.getUrl()).build();
        SpellClient spellRestClient = new SpellClient(restTemplate);
        Optional<Spell> spell = spellRestClient.getSpell("hokus pocus");
        assertTrue(spell.isEmpty());
    }

    @Pact(consumer = CONSUMER, provider = PROVIDER)
    @SuppressWarnings("unused")
    V4Pact addingNewSpellContract(PactDslWithProvider builder) {
        return builder
                .given("spell does not exist", Map.of("name", "hokus pocus"))
                .uponReceiving("a request to add a new spell")
                    .path("/spells")
                    .method("POST")
                    .headers(Map.of("Content-Type", "application/json"))
                    .body(new PactDslJsonBody()
                            .stringValue("name", "hokus pocus")
                            .stringType("description")
                    )
                .willRespondWith()
                    .status(201)
                .toPact(V4Pact.class);
    }

    @Test
    @DisplayName("validate contract for adding a new spell that does not already exist")
    @PactTestFor(pactMethod = "addingNewSpellContract")
    void validate_contract_for_adding_a_new_spell_that_does_not_already_exist(MockServer mockServer) {
        RestTemplate restTemplate = new RestTemplateBuilder().rootUri(mockServer.getUrl()).build();
        SpellClient spellRestClient = new SpellClient(restTemplate);
        Spell spell = Spell.builder().name("hokus pocus").description("Does something").build();
        assertDoesNotThrow(() -> spellRestClient.addSpell(spell));
    }

    @Pact(consumer = CONSUMER, provider = PROVIDER)
    @SuppressWarnings("unused")
    V4Pact addAlreadyExistingSpellContract(PactDslWithProvider builder) {
        return builder
                .given("spell exists", Map.of("name", "accio"))
                .uponReceiving("a request to add a new spell")
                    .path("/spells")
                    .method("POST")
                    .headers(Map.of("Content-Type", "application/json"))
                    .body(new PactDslJsonBody()
                            .stringValue("name", "accio")
                            .stringType("description")
                    )
                .willRespondWith()
                    .status(409)
                .toPact(V4Pact.class);
    }

    @Test
    @DisplayName("validate contract for adding a spell that already exists")
    @PactTestFor(pactMethod = "addAlreadyExistingSpellContract")
    void validate_contract_for_adding_a_spell_that_already_exists(MockServer mockServer) {
        RestTemplate restTemplate = new RestTemplateBuilder().rootUri(mockServer.getUrl()).build();
        SpellClient spellRestClient = new SpellClient(restTemplate);
        Spell spell = Spell.builder().name("accio").description("Summoning Charm").build();
        assertThrows(SpellAlreadyExistsException.class, () -> spellRestClient.addSpell(spell));
    }
}