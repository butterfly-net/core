package it.unibz.butterfly_net.core.agent_manager.domain.errors;

public class MissingRequiredInputError extends RuntimeException {
    public MissingRequiredInputError(String name, String type) {
        super(String.format("Input '%s' of type '%s' is missing", name, type));
    }
}
