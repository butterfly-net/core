package it.unibz.butterfly_net.core.ingestion.core;

public record RawDataEntry(
        String projectId,
        Long timestamp,
        String content
) {
}
