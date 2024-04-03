package it.unibz.butterfly_net.ingestion_api.core.errors;

public class ProjectNotFoundError extends RuntimeException {
    public ProjectNotFoundError(String projectId) {
        super("Project #" + projectId + " not found");
    }
}
