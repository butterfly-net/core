package it.unibz.butterfly_net.core.agent_manager.domain.usecases;

import it.unibz.butterfly_net.core.agent_manager.domain.errors.UnknownParserTypeError;
import it.unibz.butterfly_net.core.agent_manager.domain.model.Agent;
import it.unibz.butterfly_net.core.agent_manager.domain.model.Parser;
import it.unibz.butterfly_net.core.agent_manager.domain.model.agent.Capability;
import it.unibz.butterfly_net.core.agent_manager.domain.model.agent.ParserDescriptor;
import it.unibz.butterfly_net.core.agent_manager.domain.usecases.dto.AgentRegistrationForm;
import it.unibz.butterfly_net.core.agent_manager.domain.utils.MockAgentRepository;
import it.unibz.butterfly_net.core.agent_manager.domain.utils.MockAgentRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class RegisterAgentTest {
    private MockAgentRepository mockAgentRepository;
    private RegisterAgent underTest;

    @BeforeEach
    void setup() {
        mockAgentRepository = new MockAgentRepositoryImpl();
        underTest = new RegisterAgent(mockAgentRepository);
    }

    @Test
    void givenUnknownParserType_thenThrowsUnknownParserTypeError() {
        // given
        String unknownType = "unknown";
        AgentRegistrationForm form = simpleFormWithParserType(unknownType);

        // then ... when
        assertThrows(UnknownParserTypeError.class, () -> {
            underTest.register(form);
        });
    }

    @Test
    void givenJsParserType_thenDoesNotThrow() {
        // given
        AgentRegistrationForm form =
                simpleFormWithParserType(Parser.JS_ENGINE_TYPE);

        // then ... when
        assertDoesNotThrow(() -> underTest.register(form));
    }

    private AgentRegistrationForm simpleFormWithParserType(String type) {
        return new AgentRegistrationForm(
                "test",
                new Capability(
                        new ParserDescriptor(type, ""),
                        Set.of()
                )
        );
    }
}