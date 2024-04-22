package it.unibz.butterfly_net.core.ingestion.core;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.unibz.butterfly_net.core.ingestion.core.errors.CredentialsMismatchError;
import it.unibz.butterfly_net.core.ingestion.core.errors.MissingRequiredHeaderError;

import java.time.Instant;
import java.util.List;
import java.util.Map;

public class IngestionService {
    public static final String AUTH_HEADER = "Authentication-Key";
    public static final String PROJECT_HEADER = "Project-Id";

    private final RawDataRepository rawDataRepository;
    private final ProjectRepository projectRepository;

    public IngestionService(RawDataRepository rawDataRepository, ProjectRepository projectRepository) {
        this.rawDataRepository = rawDataRepository;
        this.projectRepository = projectRepository;
    }

    public void ingest(Map<String, String> headers, Map<String, List<String>> queryParams, String body) {
        String projectId = getProjectId(headers);
        String authHeader = getAuthHeader(headers);
        Project project = projectRepository.findById(projectId);

        boolean authMatch = project.authenticationKey().equals(authHeader);

        if (!authMatch)
            throw new CredentialsMismatchError();

        recordRequest(headers, queryParams, body, projectId);
    }

    private void recordRequest(Map<String, String> headers, Map<String, List<String>> queryParams, String body, String projectId) {
        long timestamp = getNow();
        String json = requestToJson(headers, queryParams, body);
        rawDataRepository.store(projectId, timestamp, json);
    }

    private String readHeaderOrThrow(Map<String, String> headers, String key) {
        String read = headers.get(key);

        if (read == null || read.isBlank())
            throw new MissingRequiredHeaderError(key);

        return read;
    }

    private String getProjectId(Map<String, String> headers) {
        return readHeaderOrThrow(headers, PROJECT_HEADER);
    }

    private String getAuthHeader(Map<String, String> headers) {
        return readHeaderOrThrow(headers, AUTH_HEADER);
    }

    private long getNow() {
        return Instant.now().toEpochMilli();
    }

    private String requestToJson(Map<String, String> headers, Map<String, List<String>> queryParams, String body) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(Map.of(
                    "headers", headers,
                    "queryParams", queryParams,
                    "body", body
            ));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
