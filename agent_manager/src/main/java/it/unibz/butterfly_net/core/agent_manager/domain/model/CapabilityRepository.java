package it.unibz.butterfly_net.core.agent_manager.domain.model;

public interface CapabilityRepository {
    Agent findAgentByCapabilityName(String capability);
}
