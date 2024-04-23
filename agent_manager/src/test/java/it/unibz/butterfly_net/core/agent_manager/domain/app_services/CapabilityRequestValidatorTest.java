package it.unibz.butterfly_net.core.agent_manager.domain.app_services;

import it.unibz.butterfly_net.core.agent_manager.domain.errors.CapabilityNotFoundError;
import it.unibz.butterfly_net.core.agent_manager.domain.errors.MissingRequiredInputError;
import it.unibz.butterfly_net.core.agent_manager.domain.model.Agent;
import it.unibz.butterfly_net.core.agent_manager.domain.model.CapabilityRepository;
import it.unibz.butterfly_net.core.agent_manager.domain.model.CapabilityRequest;
import it.unibz.butterfly_net.core.agent_manager.domain.model.agent.Capability;
import it.unibz.butterfly_net.core.agent_manager.domain.model.agent.ParserDescriptor;
import it.unibz.butterfly_net.core.agent_manager.domain.model.agent.ParserInputDescriptor;
import it.unibz.butterfly_net.core.agent_manager.domain.utils.MockCapabilityRepository;
import it.unibz.butterfly_net.core.agent_manager.domain.utils.MockCapabilityRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class CapabilityRequestValidatorTest {
    private MockCapabilityRepository mockCapabilityRepository;
    private CapabilityRequestValidator underTest;

    @BeforeEach
    void setup() {
        mockCapabilityRepository = new MockCapabilityRepositoryImpl();
        underTest = new CapabilityRequestValidator(mockCapabilityRepository);
    }

    @Test
    void givenNonExistingCapability_thenThrowsCapabilityNotFoundError() {
        // given
        String nonExistingCapability = "nonexisting";
        CapabilityRequest request = simpleRequestFor(nonExistingCapability);
        mockCapabilityRepository.mockFind((String name) -> {
            throw new CapabilityNotFoundError(name);
        });

        // then ... when
        assertThrows(
                CapabilityNotFoundError.class,
                () -> underTest.validate(request)
        );
    }

    @Test
    void givenMissingRequiredInput_thenThrowsMissingRequiredInputError() {
        // given
        ParserInputDescriptor inputDescriptor =
                new ParserInputDescriptor("foo", "bar", true);
        mockCapabilityRepository.mockFind(toReturnSingleInput(inputDescriptor));
        CapabilityRequest request = simpleRequestFor("test");

        // then ... when
        assertThrows(
                MissingRequiredInputError.class,
                () -> underTest.validate(request)
        );
    }

    private CapabilityRepository toReturnSingleInput(ParserInputDescriptor inputDescriptor) {
        return (String name) -> new Agent(
                "test",
                new Capability(
                        new ParserDescriptor("foo", "bar"),
                        Set.of(inputDescriptor)
                )
        );
    }

    private CapabilityRequest simpleRequestFor(String capabilityName) {
        return new CapabilityRequest(capabilityName, Set.of());
    }
}