package it.unibz.butterfly_net.core.agent_manager.domain.services.callers;

import it.unibz.butterfly_net.core.agent_manager.domain.model.Agent;
import it.unibz.butterfly_net.core.agent_manager.domain.model.AgentCaller;
import it.unibz.butterfly_net.core.agent_manager.domain.model.CapabilityRequest;

public abstract class BaseCaller implements AgentCaller {
    protected final Agent agent;
    protected final CapabilityRequest request;
    protected AgentCaller wrapped;

    protected BaseCaller(AgentCaller wrapped, Agent agent, CapabilityRequest request) {
        this.wrapped = wrapped;
        this.agent = agent;
        this.request = request;
    }

    protected abstract void before();
    protected abstract void after();

    @Override
    public void run() {
        this.before();
        this.wrapped.run();
        this.after();
    }
}
