package it.unibz.butterfly_net.core.agent_manager.domain.app_services;

import it.unibz.butterfly_net.core.agent_manager.domain.errors.MissingRequiredInputError;
import it.unibz.butterfly_net.core.agent_manager.domain.model.Agent;
import it.unibz.butterfly_net.core.agent_manager.domain.model.CapabilityRepository;
import it.unibz.butterfly_net.core.agent_manager.domain.model.CapabilityRequest;
import it.unibz.butterfly_net.core.agent_manager.domain.model.agent.ParserInputDescriptor;

import java.util.Map;
import java.util.Set;

public class CapabilityRequestValidator {
    private final CapabilityRepository capabilityRepository;

    public CapabilityRequestValidator(CapabilityRepository capabilityRepository) {
        this.capabilityRepository = capabilityRepository;
    }

    public void validate(CapabilityRequest request) {
        Agent agent = capabilityRepository.findAgentByCapabilityName(request.name());
        Set<ParserInputDescriptor> inputDescriptors = agent.getCapability().parserInputDescriptors();

        Set<Map<String, Object>> inputs = request.inputs();
        for (ParserInputDescriptor descriptor : inputDescriptors) {
            boolean containKey = inputs.stream().anyMatch(map -> map.containsKey(descriptor.name()));

            boolean isMissingRequired = descriptor.required() && !containKey;

            if (isMissingRequired)
                throw new MissingRequiredInputError(descriptor.name(), descriptor.type());
        }
    }
}
