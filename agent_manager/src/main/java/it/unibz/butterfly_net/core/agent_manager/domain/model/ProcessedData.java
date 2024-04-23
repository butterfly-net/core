package it.unibz.butterfly_net.core.agent_manager.domain.model;

public record ProcessedData(
        Long projectId,
        Long timestamp,
        String status,
        String jsonContent
) {
}
