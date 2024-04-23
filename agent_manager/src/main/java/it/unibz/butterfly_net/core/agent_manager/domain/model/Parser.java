package it.unibz.butterfly_net.core.agent_manager.domain.model;

import it.unibz.butterfly_net.core.agent_manager.domain.errors.UnknownParserTypeError;
import it.unibz.butterfly_net.core.agent_manager.domain.app_services.JavaScriptEngine;
import it.unibz.butterfly_net.core.agent_manager.domain.app_services.LambdaFunctionCaller;

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

    public static Parser ofType(String type) {
        validateType(type);

        return switch (type) {
            case JS_ENGINE_TYPE -> new JavaScriptEngine();
            default -> new LambdaFunctionCaller();
        };
    }

    public abstract ProcessedData parse(RawData rawData);
}
