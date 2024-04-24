package it.unibz.butterfly_net.core.agent_manager.domain.utils;

import it.unibz.butterfly_net.core.agent_manager.domain.model.Agent;
import it.unibz.butterfly_net.core.agent_manager.domain.model.CapabilityAgentFinder;

public class MockCapabilityAgentFinderImpl implements MockCapabilityAgentFinder {
    private CapabilityAgentFinder behavior = (String name) -> {
        throw new RuntimeException("mock not implemented");
    };

    @Override
    public Agent findAgentByCapabilityName(String capability) {
        return this.behavior.findAgentByCapabilityName(capability);
    }

    @Override
    public void mockFind(CapabilityAgentFinder mockBehavior) {
        this.behavior = mockBehavior;
    }
}
