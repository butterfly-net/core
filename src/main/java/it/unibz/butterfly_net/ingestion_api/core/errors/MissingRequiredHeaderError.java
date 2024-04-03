package it.unibz.butterfly_net.ingestion_api.core.errors;

public class MissingRequiredHeaderError extends RuntimeException {
    public MissingRequiredHeaderError(String missingHeader) {
        super("Required header '" + missingHeader + "' is missing");
    }
}
