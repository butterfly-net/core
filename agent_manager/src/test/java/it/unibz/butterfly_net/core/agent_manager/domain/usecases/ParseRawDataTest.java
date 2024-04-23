package it.unibz.butterfly_net.core.agent_manager.domain.usecases;

import it.unibz.butterfly_net.core.agent_manager.domain.errors.UnknownParserTypeError;
import it.unibz.butterfly_net.core.agent_manager.domain.model.Parser;
import it.unibz.butterfly_net.core.agent_manager.domain.model.Project;
import it.unibz.butterfly_net.core.agent_manager.domain.model.ProjectRepository;
import it.unibz.butterfly_net.core.agent_manager.domain.model.RawData;
import it.unibz.butterfly_net.core.agent_manager.domain.utils.MockProcessedDataRepository;
import it.unibz.butterfly_net.core.agent_manager.domain.utils.MockProcessedDataRepositoryImpl;
import it.unibz.butterfly_net.core.agent_manager.domain.utils.MockProjectRepository;
import it.unibz.butterfly_net.core.agent_manager.domain.utils.MockProjectRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ParseRawDataTest {
    private MockProjectRepository mockProjectRepository;
    private ParseRawData underTest;

    @BeforeEach
    void setup() {
        mockProjectRepository = new MockProjectRepositoryImpl();
        MockProcessedDataRepository mockProcessedDataRepository = new MockProcessedDataRepositoryImpl();
        underTest = new ParseRawData(mockProjectRepository, mockProcessedDataRepository);
    }

    @Test
    void givenUnknownParserType_thenThrowsUnknownParserTypeError() {
        // given
        String unknownParserType = "unknown";
        mockProjectRepository.mockFind(toReturnProjectWithParserType(unknownParserType));
        RawData input = new RawData(42L, 1234L, "{}");

        // then ... when
        UnknownParserTypeError upte = assertThrowsExactly(
                UnknownParserTypeError.class,
                () -> underTest.parse(input)
        );
        assertEquals(
                UnknownParserTypeError.errorMessage(unknownParserType),
                upte.getMessage()
        );
    }

    @Test
    void givenJsParserType_thenDoesNotThrow() {
        // given
        mockProjectRepository
                .mockFind(toReturnProjectWithParserType(Parser.JS_ENGINE_TYPE));
        RawData input = new RawData(42L, 1234L, "{}");

        // then ... when
        assertDoesNotThrow(() -> underTest.parse(input));
    }

    private ProjectRepository toReturnProjectWithParserType(String type) {
        return (id) -> new Project(id, type);
    }
}