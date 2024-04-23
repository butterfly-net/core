package it.unibz.butterfly_net.core.agent_manager.domain.utils;

import it.unibz.butterfly_net.core.agent_manager.domain.model.CapabilityRepository;

public interface MockCapabilityRepository extends CapabilityRepository {
    void mockFind(CapabilityRepository mockBehavior);
}
