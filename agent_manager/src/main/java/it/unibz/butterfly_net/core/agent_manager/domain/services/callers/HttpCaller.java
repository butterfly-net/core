package it.unibz.butterfly_net.core.agent_manager.domain.services.callers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.unibz.butterfly_net.core.agent_manager.domain.model.Agent;
import it.unibz.butterfly_net.core.agent_manager.domain.model.AgentCaller;
import it.unibz.butterfly_net.core.agent_manager.domain.model.CapabilityRequest;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class HttpCaller extends BaseCaller {
    public HttpCaller(AgentCaller wrapped, Agent agent, CapabilityRequest request) {
        super(wrapped, agent, request);
    }

    @Override
    protected void before() {
        String url = "http://localhost:9003/dispatch";
        Map<String, Object> mergedBody = this.mergeInputs(request.inputs());
        String jsonBody = this.bodyToJson(mergedBody);

        HttpRequest request = this.createPostRequest(url, jsonBody);
        this.sendRequest(request);
    }

    private String bodyToJson(Map<String, Object> body) {
        ObjectMapper mapper = new ObjectMapper();

        try {
            return mapper.writeValueAsString(body);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private Map<String, Object> mergeInputs(Set<Map<String, Object>> inputs) {
        return inputs
                .stream()
                .reduce(
                        new HashMap<>(),
                        (acc, input) -> {
                            acc.putAll(input);
                            return acc;
                        }
                );
    }

    @Override
    protected void after() {}

    private HttpRequest createPostRequest(String url, String jsonBody) {
        HttpRequest.Builder builder = HttpRequest
                .newBuilder()
                .uri(URI.create(url))
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody));

        return builder.build();
    }

    private void sendRequest(HttpRequest request) {
        java.net.http.HttpClient httpClient = java.net.http.HttpClient.newHttpClient();
        try {
            httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
