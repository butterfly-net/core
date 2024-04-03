package it.unibz.butterfly_net.ingestion_api.core;

public interface RawDataRepository {
    void store(String projectId, Long timestamp, String contentJson);
}
