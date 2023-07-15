package com.example.contracttesting.provider;

import au.com.dius.pact.core.model.Pact;
import au.com.dius.pact.provider.junit5.HttpTestTarget;
import au.com.dius.pact.provider.junit5.PactVerificationContext;
import au.com.dius.pact.provider.junitsupport.Provider;
import au.com.dius.pact.provider.junitsupport.State;
import au.com.dius.pact.provider.junitsupport.loader.PactBroker;
import au.com.dius.pact.provider.spring.junit5.PactVerificationSpringProvider;
import com.example.contracttesting.provider.api.SpellController;
import com.example.contracttesting.provider.core.Spell;
import com.example.contracttesting.provider.core.SpellFetcher;
import com.example.contracttesting.provider.core.SpellPersister;
import com.example.contracttesting.provider.core.SpellService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.mockito.Mockito.when;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = {
                SpellController.class,
                SpellService.class
        }
)
@Provider("spell-server")
@PactBroker
@EnableAutoConfiguration
class SpellServerProviderContractTest {

    @LocalServerPort
    private int port;

    @MockBean
    private SpellFetcher spellFetcher;

    @MockBean
    private SpellPersister spellPersister;

    @TestTemplate
    @ExtendWith(PactVerificationSpringProvider.class)
    void pactVerificationTestTemplate(PactVerificationContext context) {
        context.verifyInteraction();
    }

    @BeforeEach
    void setUp(PactVerificationContext context, Pact pact) {
        if (pact.isRequestResponsePact()) {
            context.setTarget(new HttpTestTarget("localhost", port));
        }
    }

    @State("spells exist")
    @SuppressWarnings("unused")
    void toSpellsExistState() {
        when(spellFetcher.getAll()).thenReturn(List.of(
                Spell.builder().name("hokus pokus").description("does anything").build(),
                Spell.builder().name("Lumos maxima").description("An improved version of the lumos spell").build()
        ));
    }

    @State("spell exists")
    @SuppressWarnings("unused")
    void toSpellExistsState(Map<String, String> parameters) {
        String spellName = parameters.get("name");
        System.out.println("nameeee " + spellName);
        when(spellFetcher.spellExists(spellName)).thenReturn(true);
        String randomDescription = UUID.randomUUID().toString();
        when(spellFetcher.getByName(spellName)).thenReturn(Spell.builder().name(spellName).description(randomDescription).build());
    }

    @State("spell does not exist")
    @SuppressWarnings("unused")
    void toSpellDoesNotExistState(Map<String, String> parameters) {
        String spellName = parameters.get("name");
        when(spellFetcher.spellExists(spellName)).thenReturn(false);
    }
}
