package it.unibz.butterfly_net.ingestion_api.application;

import it.unibz.butterfly_net.ingestion_api.core.RawDataRepository;

import java.util.ArrayList;
import java.util.List;

public class InMemoryRawDataRepository implements RawDataRepository {
    private record Entry(
            String projectId,
            Long timestamp,
            String content
    ) {}

    private List<Entry> entries = new ArrayList<>();

    @Override
    public void store(String projectId, Long timestamp, String contentJson) {
        entries.add(new Entry(projectId, timestamp, contentJson));
    }
}
