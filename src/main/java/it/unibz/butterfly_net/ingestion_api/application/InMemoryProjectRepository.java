package it.unibz.butterfly_net.ingestion_api.application;

import it.unibz.butterfly_net.ingestion_api.core.Project;
import it.unibz.butterfly_net.ingestion_api.core.ProjectRepository;
import it.unibz.butterfly_net.ingestion_api.core.errors.ProjectNotFoundError;

import java.util.Map;

public class InMemoryProjectRepository implements ProjectRepository {
    private Map<String, Project> projects = Map.of(
            "4d3c2b1a", new Project("4d3c2b1a", "4321")
    );

    @Override
    public Project findById(String projectId) {
        Project project = projects.get(projectId);

        if (project == null)
            throw new ProjectNotFoundError(projectId);

        return project;
    }
}
