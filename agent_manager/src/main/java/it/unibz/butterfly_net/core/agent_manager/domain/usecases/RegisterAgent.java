package it.unibz.butterfly_net.core.agent_manager.domain.usecases;

import it.unibz.butterfly_net.core.agent_manager.domain.model.Agent;
import it.unibz.butterfly_net.core.agent_manager.domain.model.AgentRepository;
import it.unibz.butterfly_net.core.agent_manager.domain.usecases.dto.AgentRegistrationForm;

public class RegisterAgent {

    private final AgentRepository agentRepository;

    public RegisterAgent(AgentRepository agentRepository) {
        this.agentRepository = agentRepository;
    }

    public Agent register(AgentRegistrationForm form) {
        return this.agentRepository.create(from(form));
    }

    private Agent from(AgentRegistrationForm form) {
        return new Agent(form.name(), form.capability());
    }
}
