package it.unibz.butterfly_net.core.agent_manager.domain.model;

import it.unibz.butterfly_net.core.agent_manager.domain.errors.IdReassignmentError;
import it.unibz.butterfly_net.core.agent_manager.domain.model.agent.Capability;

public class Agent {
    private Long id;
    private String name;
    private Capability capability;

    public Agent(String name, Capability capability) {
        this.name = name;
        this.capability = capability;
    }

    public void assignId(Long id) {
        if (this.id != null)
            throw new IdReassignmentError(id, this.id, Agent.class.getTypeName());

        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Capability getCapability() {
        return capability;
    }

    public void setCapability(Capability capability) {
        this.capability = capability;
    }
}
