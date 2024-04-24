package it.unibz.butterfly_net.core.agent_manager.domain.app_services;

import it.unibz.butterfly_net.core.agent_manager.domain.errors.CapabilityNotFoundError;
import it.unibz.butterfly_net.core.agent_manager.domain.errors.MissingRequiredInputError;
import it.unibz.butterfly_net.core.agent_manager.domain.errors.WrongInputTypeError;
import it.unibz.butterfly_net.core.agent_manager.domain.model.Agent;
import it.unibz.butterfly_net.core.agent_manager.domain.model.CapabilityAgentFinder;
import it.unibz.butterfly_net.core.agent_manager.domain.model.CapabilityRequest;
import it.unibz.butterfly_net.core.agent_manager.domain.model.agent.Capability;
import it.unibz.butterfly_net.core.agent_manager.domain.model.agent.ParserDescriptor;
import it.unibz.butterfly_net.core.agent_manager.domain.model.agent.ParserInputDescriptor;
import it.unibz.butterfly_net.core.agent_manager.domain.utils.MockCapabilityAgentFinder;
import it.unibz.butterfly_net.core.agent_manager.domain.utils.MockCapabilityAgentFinderImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class CapabilityRequestValidatorTest {
    private MockCapabilityAgentFinder mockCapabilityRepository;
    private CapabilityRequestValidator underTest;

    @BeforeEach
    void setup() {
        mockCapabilityRepository = new MockCapabilityAgentFinderImpl();
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

    @Test
    void givenWrongTypeInput_thenThrowsWrongInputTypeError() {
        // given
        String capabilityName = "test";
        String inputName = "attr";
        ParserInputDescriptor requiredStringNamedAttr =
            new ParserInputDescriptor("String", inputName, true);
        mockCapabilityRepository.mockFind(toReturnSingleInput(capabilityName, requiredStringNamedAttr));
        CapabilityRequest request = simpleRequestFor(capabilityName,Set.of(
                Map.of(inputName, 4L)
        ));

        // then ... when
        assertThrows(
                WrongInputTypeError.class,
                () -> underTest.validate(request)
        );
    }

    @Test
    void givenWellDefinedRequest_thenDoesNotThrow() {
        // given
        String capabilityName = "test";
        String inputName = "attr";
        String inputValue = "any string";
        String inputType = inputValue.getClass().getSimpleName();
        ParserInputDescriptor requiredStringNamedAttr =
                new ParserInputDescriptor(inputType, inputName, true);
        mockCapabilityRepository.mockFind(toReturnSingleInput(capabilityName, requiredStringNamedAttr));
        CapabilityRequest request = simpleRequestFor(capabilityName, Set.of(
                Map.of(inputName, inputValue)
        ));

        // then ... when
        assertDoesNotThrow(() -> underTest.validate(request));
    }

    @Test
    void givenWellDefinedComplexRequest_thenDoesNotThrow() {
        // given
        Scenario scenario = this.complexMatchingRequest();
        mockCapabilityRepository.mockFind(scenario.mockRepository());
        CapabilityRequest request = scenario.request();

        // then ... when
        assertDoesNotThrow(() -> underTest.validate(request));
    }

    @Test
    void givenWellDefinedComplexRequest_thenStripesExtraInputs() {
        // given
        String extraName = "foo";
        String extraValue = "bar";
        Scenario scenario = this.complexMatchingRequest(extraName, extraValue);
        mockCapabilityRepository.mockFind(scenario.mockRepository());
        CapabilityRequest request = scenario.request();

        // when
        CapabilityRequest newRequest = underTest.validate(request);

        // then
        boolean containsExtra = newRequest.inputs()
                .stream()
                .anyMatch(pair -> pair.containsKey(extraName));

        assertFalse(containsExtra);
    }

    private record Scenario(
            CapabilityAgentFinder mockRepository,
            CapabilityRequest request
    ) {}

    private Scenario complexMatchingRequest() {
        return complexMatchingRequest("unforeseen", "whatever");
    }

    private Scenario complexMatchingRequest(String extraName, String extraValue) {
        CapabilityAgentFinder mockRepository = (capabilityName -> new Agent(
                capabilityName,
                new Capability(null, Set.of(
                        new ParserInputDescriptor("Long", "id", true),
                        new ParserInputDescriptor("String", "name", true),
                        new ParserInputDescriptor("String", "description", false)
                ))
        ));

        CapabilityRequest request = new CapabilityRequest(
                "test", Set.of(
                        Map.of("id", 42L),
                        Map.of("name", "foo"),
                        Map.of(extraName, extraValue)
                )
        );

        return new Scenario(mockRepository, request);
    }

    private CapabilityAgentFinder toReturnSingleInput(ParserInputDescriptor inputDescriptor) {
        return toReturnSingleInput("test", inputDescriptor);
    }

    private CapabilityAgentFinder toReturnSingleInput(
            String capabilityName,
            ParserInputDescriptor inputDescriptor
    ) {
        return (String name) -> new Agent(
                capabilityName,
                new Capability(
                        new ParserDescriptor("foo", "bar"),
                        Set.of(inputDescriptor)
                )
        );
    }

    private CapabilityRequest simpleRequestFor(String capabilityName) {
        return simpleRequestFor(capabilityName, Set.of());
    }

    private CapabilityRequest simpleRequestFor(String capabilityName, Set<Map<String,Object>> inputs) {
        return new CapabilityRequest(capabilityName, inputs);
    }
}