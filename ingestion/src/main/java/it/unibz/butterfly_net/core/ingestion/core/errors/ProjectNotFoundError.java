package it.unibz.butterfly_net.core.ingestion.core.errors;

public class ProjectNotFoundError extends RuntimeException {
    public ProjectNotFoundError(String projectId) {
        super("Project #" + projectId + " not found");
    }
}
