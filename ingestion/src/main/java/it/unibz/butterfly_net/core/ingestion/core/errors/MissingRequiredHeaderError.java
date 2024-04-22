package it.unibz.butterfly_net.core.ingestion.core.errors;

public class MissingRequiredHeaderError extends RuntimeException {
    public MissingRequiredHeaderError(String missingHeader) {
        super("Required header '" + missingHeader + "' is missing");
    }
}
