package it.unibz.butterfly_net.core.agent_manager.domain.model;

import it.unibz.butterfly_net.core.agent_manager.domain.errors.UnknownParserTypeError;

import java.util.List;

public abstract class Parser {
    public static final String JS_ENGINE_TYPE = "js_engine";
    public static final String LAMBDA_CALLER_TYPE = "lambda_caller";

    public static void validateType(String type) {
        List<String> knownTypes = List.of(JS_ENGINE_TYPE, LAMBDA_CALLER_TYPE);
        boolean isKnownType = knownTypes.contains(type);

        if (!isKnownType)
            throw new UnknownParserTypeError(type);
    }
}
