package it.unibz.butterfly_net.ingestion_api.core;

public record RawDataEntry(
        String projectId,
        Long timestamp,
        String content
) {
}
