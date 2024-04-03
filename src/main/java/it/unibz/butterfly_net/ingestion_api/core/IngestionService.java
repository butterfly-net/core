package it.unibz.butterfly_net.ingestion_api.core;

import it.unibz.butterfly_net.ingestion_api.core.utils.Config;
import it.unibz.butterfly_net.ingestion_api.core.errors.CredentialsMismatchError;
import it.unibz.butterfly_net.ingestion_api.core.errors.MissingRequiredHeaderError;

import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.Map;

public class IngestionService {
    public static final String AUTH_HEADER;
    public static final String PROJECT_HEADER;

    static {
        try {
            AUTH_HEADER = Config.getInstance().property("AUTHENTICATION_HEADER_KEY");
            PROJECT_HEADER = Config.getInstance().property("PROJECT_HEADER_KEY");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

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

    // TODO: properly parse to JSON
    @SuppressWarnings("unused")
    private String requestToJson(Map<String, String> headers, Map<String, List<String>> queryParams, String body) {
        return "";
    }
}
