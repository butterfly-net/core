package it.unibz.butterfly_net.core.agent_manager.domain.utils;

import it.unibz.butterfly_net.core.agent_manager.domain.model.AgentRepository;

public interface MockAgentRepository extends AgentRepository {
    void mockCreate(AgentRepository mockedBehavior);
}
