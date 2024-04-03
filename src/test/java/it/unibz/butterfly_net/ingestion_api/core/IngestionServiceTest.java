package it.unibz.butterfly_net.ingestion_api.core;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.unibz.butterfly_net.ingestion_api.core.errors.CredentialsMismatchError;
import it.unibz.butterfly_net.ingestion_api.core.errors.MissingRequiredHeaderError;
import it.unibz.butterfly_net.ingestion_api.core.errors.ProjectNotFoundError;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class IngestionServiceTest {
    private IngestionService underTest;

    @Test
    void givenNoProjectIdHeader_itThrowsMissingHeader() {
        // given
        Map<String, String> onlyAuthHeaders = Map.of(
                IngestionService.AUTH_HEADER, "abcd-1234"
        );
        Map<String, List<String>> emptyQueryParams = Map.of();
        String emptyBody = "";
        underTest = new IngestionService(blankRawDataRepo(), blankProjectRepo());

        // when ... then
        assertThrows(
                MissingRequiredHeaderError.class,
                () -> underTest.ingest(onlyAuthHeaders, emptyQueryParams, emptyBody)
        );
    }

    @Test
    void givenNoAuthKeyHeader_itThrowsMissingHeader() {
        // given
        Map<String, String> onlyProjectIdHeader = Map.of(
                IngestionService.PROJECT_HEADER, "abcd-1234"
        );
        Map<String, List<String>> emptyQueryParams = Map.of();
        String emptyBody = "";
        underTest = new IngestionService(blankRawDataRepo(), blankProjectRepo());

        // when ... then
        assertThrows(
                MissingRequiredHeaderError.class,
                () -> underTest.ingest(onlyProjectIdHeader, emptyQueryParams, emptyBody)
        );
    }

    @Test
    void givenUnknownProjectIdHeader_itThrowsProjectNotFound() {
        // given
        Map<String, String> headers = Map.of(
                IngestionService.PROJECT_HEADER, "abcd-1234",
                IngestionService.AUTH_HEADER, "abcd-1234"
        );
        Map<String, List<String>> emptyQueryParams = Map.of();
        String emptyBody = "";
        underTest = new IngestionService(blankRawDataRepo(), noProjectsRepo());

        // when ... then
        assertThrows(
                ProjectNotFoundError.class,
                () -> underTest.ingest(headers, emptyQueryParams, emptyBody)
        );
    }

    @Test
    void givenWrongAuthKeyHeader_itThrowsCredentialsMismatch() {
        // given
        String projectId = "abcd-1234";
        String authKey = "dcba-4321";
        String wrongKey = "4321-dcba";
        Map<String, String> headers = Map.of(
                IngestionService.PROJECT_HEADER, projectId,
                IngestionService.AUTH_HEADER, wrongKey
        );
        Map<String, List<String>> emptyQueryParams = Map.of();
        String emptyBody = "";
        underTest = new IngestionService(blankRawDataRepo(), seededProjectRepo(projectId, authKey));

        // when ... then
        assertThrows(
                CredentialsMismatchError.class,
                () -> underTest.ingest(headers, emptyQueryParams, emptyBody)
        );
    }

    @Test
    void givenCorrectAuthKeyHeader_itStoresNewRawDataEntry() {
        // given
        String projectId = "abcd-1234";
        String authKey = "dcba-4321";
        Map<String, String> headers = Map.of(
                IngestionService.PROJECT_HEADER, projectId,
                IngestionService.AUTH_HEADER, authKey
        );
        Map<String, List<String>> emptyQueryParams = Map.of();
        String emptyBody = "";
        SpyRawDataRepository spyRawDataRepository = spyDataRepo();
        underTest = new IngestionService(spyRawDataRepository, seededProjectRepo(projectId, authKey));

        // when
        underTest.ingest(headers, emptyQueryParams, emptyBody);

        // ... then
        RawDataEntry lastEntry = spyRawDataRepository.getLastEntry();
        String expectedJson = generateExpectedJson(headers, emptyQueryParams, emptyBody);
        assertNotNull(lastEntry);
        assertEquals(lastEntry.projectId(), projectId);
        assertEquals(lastEntry.content(), expectedJson);
    }

    private String generateExpectedJson(Map<String, String> headers, Map<String, List<String>> queryParams, String body) {
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

    private SpyRawDataRepository spyDataRepo() {
        return new SpyRawDataRepository() {
            private RawDataEntry lastEntry = null;

            @Override
            public RawDataEntry getLastEntry() {
                return lastEntry;
            }

            @Override
            public void store(String projectId, Long timestamp, String contentJson) {
                lastEntry = new RawDataEntry(projectId, timestamp, contentJson);
            }
        };
    }

    private ProjectRepository seededProjectRepo(String seedProjectId, String seedAuthKey) {
        return new ProjectRepository() {
            @Override
            public Project findById(String projectId) {
                return new Project(seedProjectId, seedAuthKey);
            }
        };
    }

    private ProjectRepository noProjectsRepo() {
        return new ProjectRepository() {
            @Override
            public Project findById(String projectId) {
                throw new ProjectNotFoundError(projectId);
            }
        };
    }

    private ProjectRepository blankProjectRepo() {
        return null;
    }

    private RawDataRepository blankRawDataRepo() {
        return null;
    }
}