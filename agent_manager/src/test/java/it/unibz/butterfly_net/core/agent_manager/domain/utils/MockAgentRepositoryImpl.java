package it.unibz.butterfly_net.core.agent_manager.domain.utils;

import it.unibz.butterfly_net.core.agent_manager.domain.model.Agent;
import it.unibz.butterfly_net.core.agent_manager.domain.model.AgentRepository;

public class MockAgentRepositoryImpl implements MockAgentRepository {
    private AgentRepository behavior = (Agent agent) -> null;

    @Override
    public Agent create(Agent agent) {
        return this.behavior.create(agent);
    }

    @Override
    public void mockCreate(AgentRepository mockedBehavior) {
        this.behavior = mockedBehavior;
    }
}
