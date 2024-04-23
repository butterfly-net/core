package it.unibz.butterfly_net.core.agent_manager.domain.model;

public record RawData(
        Long projectId,
        Long timestamp,
        String jsonContent
) {
}
