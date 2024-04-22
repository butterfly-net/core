package it.unibz.butterfly_net.core.ingestion.core;

public interface ProjectRepository {
    Project findById(String projectId);
}
