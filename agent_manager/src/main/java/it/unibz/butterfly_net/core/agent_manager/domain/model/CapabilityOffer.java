package it.unibz.butterfly_net.core.agent_manager.domain.model;

import it.unibz.butterfly_net.core.agent_manager.domain.model.agent.ParserInputDescriptor;

import java.util.Set;

public record CapabilityOffer(
        String name,
        Set<ParserInputDescriptor> inputDescriptors
) {
}
