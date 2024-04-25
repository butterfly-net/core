package it.unibz.butterfly_net.core.agent_manager.domain.services.callers;

import it.unibz.butterfly_net.core.agent_manager.domain.model.AgentCaller;

public class NullCaller implements AgentCaller {
    @Override
    public void run() {}
}
