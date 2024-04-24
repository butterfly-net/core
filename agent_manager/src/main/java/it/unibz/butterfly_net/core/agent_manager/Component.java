package it.unibz.butterfly_net.core.agent_manager;

import io.javalin.Javalin;
import it.unibz.butterfly_net.core.agent_manager.application.InMemoryDatabase;
import it.unibz.butterfly_net.core.agent_manager.application.JavalinServerModule;
import it.unibz.butterfly_net.core.agent_manager.domain.services.AgentCallerFactory;
import it.unibz.butterfly_net.core.agent_manager.domain.services.CapabilityRequestValidator;

public class Component {
    public static void main(String[] args) {
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

        app.start(8080);
    }
}
