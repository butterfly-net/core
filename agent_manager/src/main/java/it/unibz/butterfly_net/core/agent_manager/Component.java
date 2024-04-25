package it.unibz.butterfly_net.core.agent_manager;

import io.javalin.Javalin;
import it.unibz.butterfly_net.core.agent_manager.application.InMemoryDatabase;
import it.unibz.butterfly_net.core.agent_manager.application.JavalinServerModule;
import it.unibz.butterfly_net.core.agent_manager.domain.services.AgentCallerFactory;
import it.unibz.butterfly_net.core.agent_manager.domain.services.CapabilityRequestValidator;
import it.unibz.butterfly_net.core.utils.Config;

import java.io.IOException;

public class Component {
    public static void main(String[] args) throws IOException {
        String portConfig = Config.getInstance().property("AGENT_MANAGER_SERVER_PORT");

        Javalin app = Javalin.create();
        JavalinServerModule module = new JavalinServerModule(app);

        InMemoryDatabase inMemoryDatabase = new InMemoryDatabase();
        CapabilityRequestValidator validator = new CapabilityRequestValidator(inMemoryDatabase);
        AgentCallerFactory agentCallerFactory = new AgentCallerFactory();


        module
                .setAgentRepository(inMemoryDatabase)
                .setCapabilityAgentFinder(inMemoryDatabase)
                .setAgentCallerFactory(agentCallerFactory)
                .setCapabilityFinder(inMemoryDatabase)
                .setCapabilityRequestValidator(validator)
                .install();

        app.start(Integer.parseInt(portConfig));
    }
}
