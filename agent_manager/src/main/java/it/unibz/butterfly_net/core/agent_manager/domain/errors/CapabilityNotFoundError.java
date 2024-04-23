package it.unibz.butterfly_net.core.agent_manager.domain.errors;

public class CapabilityNotFoundError extends RuntimeException {
    public CapabilityNotFoundError(String name) {
        super(String.format("Capability '%s' not found", name));
    }
}
