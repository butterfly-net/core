package it.unibz.butterfly_net.core.ingestion.core;

public interface RawDataRepository {
    void store(String projectId, Long timestamp, String contentJson);
}
