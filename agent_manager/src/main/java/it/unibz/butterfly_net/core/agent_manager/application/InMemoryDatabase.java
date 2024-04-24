package it.unibz.butterfly_net.core.agent_manager.application;

import it.unibz.butterfly_net.core.agent_manager.domain.errors.CapabilityNotFoundError;
import it.unibz.butterfly_net.core.agent_manager.domain.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class InMemoryDatabase implements AgentRepository, CapabilityAgentFinder, CapabilityFinder {
    private List<Agent> agents = new ArrayList<>();

    @Override
    public Agent create(Agent agent) {
        agent.assignId((long) this.agents.size() + 1);
        this.agents.add(agent);

        return agent;
    }

    @Override
    public Agent findAgentByCapabilityName(String capability) {
        Optional<Agent> maybeAgent = this.agents
                .stream()
                .filter(agent -> agent.getName().equals(capability))
                .findFirst();

        if (maybeAgent.isEmpty())
            throw new CapabilityNotFoundError(capability);

        return maybeAgent.get();
    }

    @Override
    public List<CapabilityOffer> getAll() {
        return this.agents
                .stream()
                .map(agent -> new CapabilityOffer(
                        agent.getName(),
                        agent.getCapability().parserInputDescriptors()
                ))
                .toList();
    }
}
