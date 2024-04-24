package it.unibz.butterfly_net.core.agent_manager.application;

import java.util.Map;
import java.util.Set;

public record CapabilityRequestDTO(
        Set<Map<String, Object>> inputs
) {
}
