package it.unibz.butterfly_net.ingestion_api.core;

public interface ProjectRepository {
    Project findById(String projectId);
}
