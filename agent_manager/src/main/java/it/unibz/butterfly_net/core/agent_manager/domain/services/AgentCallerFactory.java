package it.unibz.butterfly_net.core.agent_manager.domain.services;

import it.unibz.butterfly_net.core.agent_manager.domain.model.Agent;
import it.unibz.butterfly_net.core.agent_manager.domain.model.AgentCaller;
import it.unibz.butterfly_net.core.agent_manager.domain.model.CapabilityRequest;
import it.unibz.butterfly_net.core.agent_manager.domain.services.callers.BaseCaller;
import it.unibz.butterfly_net.core.agent_manager.domain.services.callers.HttpCaller;
import it.unibz.butterfly_net.core.agent_manager.domain.services.callers.NullCaller;
import it.unibz.butterfly_net.core.utils.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;

public class AgentCallerFactory {
    private final Logger logger = LoggerFactory.getLogger(AgentCallerFactory.class);
    private final String[] callerConfigs;
    private Agent agent = null;
    private CapabilityRequest request = null;

    public AgentCallerFactory() {
        String strategyConfig = "default";
        try {
            strategyConfig = Config.getInstance().property("AGENT_CALLER_CONFIGURATION");
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            this.callerConfigs = strategyConfig.split(",");
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

        AgentCaller caller = new NullCaller();

        for (String config : this.callerConfigs) {
            caller = this.wrap(caller, config);
        }


        this.agent = null;
        this.request = null;

        return caller;
    }

    private AgentCaller wrap(AgentCaller caller, String config) {
        return switch (config) {
            case "logger" -> new it.unibz.butterfly_net.core.agent_manager.domain.services.callers.Logger(caller, this.agent, this.request);
            case "http" -> new HttpCaller(caller, this.agent, this.request);
            default -> throw new RuntimeException();
        };
    }

    private void ensureAgent() {
        if (this.agent == null)
            throw new RuntimeException("no agent defined");
    }

    private void ensureRequest() {
        if (this.request == null)
            throw new RuntimeException("no request defined");
    }
}
