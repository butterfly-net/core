package it.unibz.butterfly_net.core.ingestion.core.errors;

public class CredentialsMismatchError extends RuntimeException {
    public CredentialsMismatchError() {
        super("Credentials don't match");
    }
}
