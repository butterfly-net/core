package it.unibz.butterfly_net.core.agent_manager.domain.utils;

import it.unibz.butterfly_net.core.agent_manager.domain.model.CapabilityAgentFinder;

public interface MockCapabilityAgentFinder extends CapabilityAgentFinder {
    void mockFind(CapabilityAgentFinder mockBehavior);
}
