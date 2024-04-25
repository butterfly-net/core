package it.unibz.butterfly_net.core.agent_manager.domain.services.callers;

import it.unibz.butterfly_net.core.agent_manager.domain.model.Agent;
import it.unibz.butterfly_net.core.agent_manager.domain.model.AgentCaller;
import it.unibz.butterfly_net.core.agent_manager.domain.model.CapabilityRequest;
import org.slf4j.LoggerFactory;

public class Logger extends BaseCaller {
    private final org.slf4j.Logger logger = LoggerFactory.getLogger(Logger.class);

    public Logger(AgentCaller wrapped, Agent agent, CapabilityRequest request) {
        super(wrapped, agent, request);
    }

    @Override
    protected void before() {
        String firstLine = String.format(
                "Preparing agent call to %s#%d",
                this.agent.getName(), this.agent.getId()
        );
        String secondLine = String.format(
                "\twith request %s",
                this.request
        );
        this.logger.info(firstLine);
        this.logger.info(secondLine);
    }

    @Override
    protected void after() {
        String message = String.format(
                "Agent call to %s#%d done",
                this.agent.getName(), this.agent.getId()
        );
        this.logger.info(message);
    }
}
