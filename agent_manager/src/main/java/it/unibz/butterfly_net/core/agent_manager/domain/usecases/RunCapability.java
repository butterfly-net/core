package it.unibz.butterfly_net.core.agent_manager.domain.usecases;

import it.unibz.butterfly_net.core.agent_manager.domain.app_services.AgentCallerFactory;
import it.unibz.butterfly_net.core.agent_manager.domain.app_services.CapabilityRequestValidator;
import it.unibz.butterfly_net.core.agent_manager.domain.model.Agent;
import it.unibz.butterfly_net.core.agent_manager.domain.model.AgentCaller;
import it.unibz.butterfly_net.core.agent_manager.domain.model.CapabilityAgentFinder;
import it.unibz.butterfly_net.core.agent_manager.domain.model.CapabilityRequest;

public class RunCapability {
    private final CapabilityRequestValidator capabilityRequestValidator;
    private final CapabilityAgentFinder capabilityAgentFinder;
    private final AgentCallerFactory agentCallerFactory;

    public RunCapability(
            CapabilityRequestValidator capabilityRequestValidator,
            CapabilityAgentFinder capabilityAgentFinder,
            AgentCallerFactory agentCallerFactory
    ) {
        this.capabilityRequestValidator = capabilityRequestValidator;
        this.capabilityAgentFinder = capabilityAgentFinder;
        this.agentCallerFactory = agentCallerFactory;
    }

    void run(CapabilityRequest request) {
        CapabilityRequest validRequest = this.capabilityRequestValidator.validate(request);
        Agent agent = this.capabilityAgentFinder.findAgentByCapabilityName(validRequest.name());
        AgentCaller agentCaller = this.prepareAgentCall(agent, validRequest);
        agentCaller.run();
    }

    private AgentCaller prepareAgentCall(Agent agent, CapabilityRequest request) {
        return this.agentCallerFactory
                .toCallAgent(agent)
                .basedOn(request)
                .get();
    }
}
