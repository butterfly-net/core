package it.unibz.butterfly_net.core.agent_manager.domain.model;

import it.unibz.butterfly_net.core.agent_manager.domain.errors.IdReassignmentError;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AgentTest {

    @Test
    void assignId() {
        // given
        Agent agent = new Agent("test", null);
        agent.assignId(1L);

        // when ... then
        assertThrows(IdReassignmentError.class, () -> agent.assignId(2L));
    }
}