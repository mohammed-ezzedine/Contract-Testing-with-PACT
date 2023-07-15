package com.example.contracttesting.provider;

import au.com.dius.pact.consumer.MessagePactBuilder;
import au.com.dius.pact.consumer.dsl.PactDslJsonBody;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.consumer.junit5.ProviderType;
import au.com.dius.pact.core.model.PactSpecVersion;
import au.com.dius.pact.core.model.V4Interaction;
import au.com.dius.pact.core.model.V4Pact;
import au.com.dius.pact.core.model.annotations.Pact;
import com.example.contracttesting.provider.messaging.event.NewSpellRequestedEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.IOException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(PactConsumerTestExt.class)
@PactTestFor(providerName = "spell-client", providerType = ProviderType.ASYNCH, pactVersion = PactSpecVersion.V4)
class AddingNewSpellAsyncConsumerContractTest {

    @Pact(consumer = "spell-server")
    @SuppressWarnings("unused")
    V4Pact newSpellRequestEventContract(MessagePactBuilder builder) {
        return builder
                .expectsToReceive("a new spell requested event")
                .withMetadata(Map.of(
                        "routing-key", "spells.add",
                        "exchange", "spells"
                ))
                .withContent(new PactDslJsonBody()
                        .stringType("name")
                        .stringType("description")
                )
                .toPact(V4Pact.class);
    }

    @Test
    @DisplayName("validate contract for new spell requested event")
    @PactTestFor(pactMethod = "newSpellRequestEventContract")
    void validate_contract_for_new_spell_requested_event(V4Interaction.AsynchronousMessage message) throws IOException {
        NewSpellRequestedEvent event = new ObjectMapper().readValue(message.getContents().getContents().getValue(), NewSpellRequestedEvent.class);
        assertNotNull(event);
    }

}
