package com.example.contracttesting.consumer.infra.http;


import au.com.dius.pact.core.model.Pact;
import au.com.dius.pact.provider.MessageAndMetadata;
import au.com.dius.pact.provider.PactVerifyProvider;
import au.com.dius.pact.provider.junit5.MessageTestTarget;
import au.com.dius.pact.provider.junit5.PactVerificationContext;
import au.com.dius.pact.provider.junitsupport.Provider;
import au.com.dius.pact.provider.junitsupport.loader.PactBroker;
import au.com.dius.pact.provider.spring.junit5.PactVerificationSpringProvider;
import com.example.contracttesting.consumer.infra.messaging.RabbitProperties;
import com.example.contracttesting.consumer.infra.messaging.event.NewSpellRequestedEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;
import java.util.UUID;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.NONE,
        classes = RabbitProperties.class
)
@Provider("spell-client") // for PACT to know to fetch the contracts for provider "spell-client"
@PactBroker
class SpellClientProviderContractTest {

    @Autowired
    private RabbitProperties rabbitProperties;

    @TestTemplate
    @ExtendWith(PactVerificationSpringProvider.class)
    void pactVerificationTestTemplate(PactVerificationContext context) {
        context.verifyInteraction();
    }

    @BeforeEach
    void setUp(PactVerificationContext context, Pact pact) {
        if (!pact.isRequestResponsePact()) {
            context.setTarget(new MessageTestTarget());
        }
    }

    @PactVerifyProvider("a new spell requested event")
    @SuppressWarnings("unused")
    MessageAndMetadata provideNewSpellRequestEvent() throws JsonProcessingException {
        NewSpellRequestedEvent event = NewSpellRequestedEvent.builder().name(UUID.randomUUID().toString()).description(UUID.randomUUID().toString()).build();
        byte[] eventBytes = new ObjectMapper().writeValueAsBytes(event);
        Map<String, String> metadata = Map.of(
                "routing-key", rabbitProperties.getAddSpellRoutingKey(),
                "exchange", rabbitProperties.getAddSpellExchange()
        );
        return new MessageAndMetadata(eventBytes, metadata);
    }
}
