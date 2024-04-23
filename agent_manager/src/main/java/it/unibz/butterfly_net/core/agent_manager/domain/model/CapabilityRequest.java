package it.unibz.butterfly_net.core.agent_manager.domain.model;

import java.util.Map;
import java.util.Set;

public record CapabilityRequest(
        String name,
        Set<Map<String, Object>> inputs
) {
}
