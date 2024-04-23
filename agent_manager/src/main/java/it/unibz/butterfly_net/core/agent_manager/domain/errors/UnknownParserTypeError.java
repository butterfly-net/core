package it.unibz.butterfly_net.core.agent_manager.domain.errors;

public class UnknownParserTypeError extends RuntimeException {
    public UnknownParserTypeError(String type) {
        super("Unknown parser type '" + type + "'");
    }
}
