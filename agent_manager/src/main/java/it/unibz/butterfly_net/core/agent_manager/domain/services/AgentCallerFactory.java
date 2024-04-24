package it.unibz.butterfly_net.core.agent_manager.domain.services;

import it.unibz.butterfly_net.core.agent_manager.domain.model.Agent;
import it.unibz.butterfly_net.core.agent_manager.domain.model.AgentCaller;
import it.unibz.butterfly_net.core.agent_manager.domain.model.CapabilityRequest;
import it.unibz.butterfly_net.core.utils.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class AgentCallerFactory {
    private final Logger logger = LoggerFactory.getLogger(AgentCallerFactory.class);
    private final String strategy;
    private Agent agent = null;
    private CapabilityRequest request = null;

    public AgentCallerFactory() {
        String strategyConfig = "default";
        try {
            strategyConfig = Config.getInstance().property("AGENT_CALLER_STRATEGY");
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            this.strategy = strategyConfig;
        }
    }

    public AgentCallerFactory toCallAgent(Agent agent) {
        this.agent = agent;
        return this;
    }

    public AgentCallerFactory basedOn(CapabilityRequest request) {
        this.request = request;
        return this;
    }

    public AgentCaller get() {
        this.ensureAgent();
        this.ensureRequest();

        AgentCaller caller = switch (this.strategy) {
            case "logger" -> loggerCaller(this.agent, this.request);
            default -> emptyCaller();
        };

        this.agent = null;
        this.request = null;

        return caller;
    }

    private void ensureAgent() {
        if (this.agent == null)
            throw new RuntimeException("no agent defined");
    }

    private void ensureRequest() {
        if (this.request == null)
            throw new RuntimeException("no request defined");
    }

    private AgentCaller emptyCaller() {
        return () -> {};
    }

    private AgentCaller loggerCaller(Agent agent, CapabilityRequest request) {
        return () -> {
            String logLine = String.format(
                    "Calling %s#%d with %s",
                    agent.getName(), agent.getId(), request.inputs()
            );
            logger.info(logLine);
        };
    }
}
