package it.unibz.butterfly_net.ingestion_api.core.errors;

public class CredentialsMismatchError extends RuntimeException {
    public CredentialsMismatchError() {
        super("Credentials don't match");
    }
}
