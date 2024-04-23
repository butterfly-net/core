package it.unibz.butterfly_net.core.agent_manager.domain.utils;

import it.unibz.butterfly_net.core.agent_manager.domain.model.Agent;
import it.unibz.butterfly_net.core.agent_manager.domain.model.CapabilityRepository;

public class MockCapabilityRepositoryImpl implements MockCapabilityRepository {
    private CapabilityRepository behavior = (String name) -> {
        throw new RuntimeException("mock not implemented");
    };

    @Override
    public Agent findAgentByCapabilityName(String capability) {
        return this.behavior.findAgentByCapabilityName(capability);
    }

    @Override
    public void mockFind(CapabilityRepository mockBehavior) {
        this.behavior = mockBehavior;
    }
}
