package it.unibz.butterfly_net.core.agent_manager.domain.errors;

public class WrongInputTypeError extends RuntimeException {
    public WrongInputTypeError(String attribute, String expectedType, String givenType) {
        super(
                String.format(
                        "Wrong type for attribute '%s' - expected '%s', but got '%s'",
                        attribute, expectedType, givenType
                )
        );
    }
}
