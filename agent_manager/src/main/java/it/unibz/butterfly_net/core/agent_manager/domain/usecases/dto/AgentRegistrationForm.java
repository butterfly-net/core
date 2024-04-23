package it.unibz.butterfly_net.core.agent_manager.domain.usecases.dto;

import it.unibz.butterfly_net.core.agent_manager.domain.model.agent.Capability;

public record AgentRegistrationForm(
        String name,
        Capability capability
) {
}
