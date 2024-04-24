package it.unibz.butterfly_net.core.agent_manager.application;

import io.javalin.Javalin;
import io.javalin.http.ExceptionHandler;
import io.javalin.http.HttpStatus;
import it.unibz.butterfly_net.core.agent_manager.domain.errors.CapabilityNotFoundError;
import it.unibz.butterfly_net.core.agent_manager.domain.errors.MissingRequiredInputError;
import it.unibz.butterfly_net.core.agent_manager.domain.errors.UnknownParserTypeError;
import it.unibz.butterfly_net.core.agent_manager.domain.errors.WrongInputTypeError;
import it.unibz.butterfly_net.core.agent_manager.domain.model.*;
import it.unibz.butterfly_net.core.agent_manager.domain.services.AgentCallerFactory;
import it.unibz.butterfly_net.core.agent_manager.domain.services.CapabilityRequestValidator;
import it.unibz.butterfly_net.core.agent_manager.domain.usecases.ListCapabilities;
import it.unibz.butterfly_net.core.agent_manager.domain.usecases.RegisterAgent;
import it.unibz.butterfly_net.core.agent_manager.domain.usecases.RunCapability;
import it.unibz.butterfly_net.core.agent_manager.domain.usecases.dto.AgentRegistrationForm;

import java.util.List;
import java.util.Map;

public class JavalinServerModule {
    private final Javalin baseServer;
    private CapabilityFinder capabilityFinder;
    private AgentRepository agentRepository;
    private CapabilityRequestValidator capabilityRequestValidator;
    private CapabilityAgentFinder capabilityAgentFinder;
    private AgentCallerFactory agentCallerFactory;

    public JavalinServerModule(Javalin server) {
        this.baseServer = server;
    }

    public JavalinServerModule setCapabilityFinder(CapabilityFinder capabilityFinder) {
        this.capabilityFinder = capabilityFinder;
        return this;
    }

    public JavalinServerModule setAgentRepository(AgentRepository agentRepository) {
        this.agentRepository = agentRepository;
        return this;
    }

    public JavalinServerModule setCapabilityRequestValidator(CapabilityRequestValidator capabilityRequestValidator) {
        this.capabilityRequestValidator = capabilityRequestValidator;
        return this;
    }

    public JavalinServerModule setCapabilityAgentFinder(CapabilityAgentFinder capabilityAgentFinder) {
        this.capabilityAgentFinder = capabilityAgentFinder;
        return this;
    }

    public JavalinServerModule setAgentCallerFactory(AgentCallerFactory agentCallerFactory) {
        this.agentCallerFactory = agentCallerFactory;
        return this;
    }

    public void install() {
        this.configureExceptionHandlers();
        this.configureEndpoints();
    }

    private void configureExceptionHandlers() {
        List.of(
                CapabilityNotFoundError.class,
                MissingRequiredInputError.class,
                WrongInputTypeError.class,
                UnknownParserTypeError.class)
                .forEach(exceptionClass ->
                        this.baseServer.exception(exceptionClass, this.badRequest()));
    }

    private void configureEndpoints() {
        ListCapabilities listCapabilities = new ListCapabilities(this.capabilityFinder);
        RegisterAgent registerAgent = new RegisterAgent(this.agentRepository);
        RunCapability runCapability =
                new RunCapability(this.capabilityRequestValidator, this.capabilityAgentFinder, this.agentCallerFactory);

        this.baseServer.get("/capabilities", ctx -> {
            List<CapabilityOffer> offers = listCapabilities.listAllCapabilities();
            ctx.status(HttpStatus.OK);
            ctx.json(Map.of("capabilities", offers));
        });

        this.baseServer.post("/agents", ctx -> {
            AgentRegistrationForm form = ctx.bodyAsClass(AgentRegistrationForm.class);
            Agent agent = registerAgent.register(form);
            ctx.status(HttpStatus.CREATED);
            ctx.json(Map.of("agent", agent));
        });

        this.baseServer.post("/capabilities/{name}/requests", ctx -> {
            String name = ctx.pathParam("name");
            CapabilityRequestDTO dto = ctx.bodyAsClass(CapabilityRequestDTO.class);
            CapabilityRequest request = new CapabilityRequest(name, dto.inputs());
            runCapability.run(request);
            ctx.status(HttpStatus.OK);
        });
    }

    private ExceptionHandler badRequest() {
        return (exception, ctx) -> {
            ctx.status(HttpStatus.BAD_REQUEST);
            ctx.json(Map.of("error", exception.getMessage()));
        };
    }
}
