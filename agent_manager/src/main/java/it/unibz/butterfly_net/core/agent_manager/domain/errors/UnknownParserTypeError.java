package it.unibz.butterfly_net.core.agent_manager.domain.errors;

public class UnknownParserTypeError extends RuntimeException {
    public static String errorMessage(String type) {
        return String.format("Unknown parser type '%s'", type);
    }

    public UnknownParserTypeError(String type) {
        super(errorMessage(type));
    }
}
