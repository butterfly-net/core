package it.unibz.butterfly_net.core.agent_manager.domain.model.agent;

import java.util.Set;

public record Capability(
        ParserDescriptor parserDescriptor,
        Set<ParserInputDescriptor> parserInputDescriptors
) {
}
