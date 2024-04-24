package it.unibz.butterfly_net.core.agent_manager.domain.app_services;

import it.unibz.butterfly_net.core.agent_manager.domain.errors.MissingRequiredInputError;
import it.unibz.butterfly_net.core.agent_manager.domain.errors.WrongInputTypeError;
import it.unibz.butterfly_net.core.agent_manager.domain.model.Agent;
import it.unibz.butterfly_net.core.agent_manager.domain.model.CapabilityAgentFinder;
import it.unibz.butterfly_net.core.agent_manager.domain.model.CapabilityRequest;
import it.unibz.butterfly_net.core.agent_manager.domain.model.agent.ParserInputDescriptor;

import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class CapabilityRequestValidator {
    private final CapabilityAgentFinder capabilityAgentFinder;

    public CapabilityRequestValidator(CapabilityAgentFinder capabilityAgentFinder) {
        this.capabilityAgentFinder = capabilityAgentFinder;
    }

    public CapabilityRequest validate(CapabilityRequest request) {
        Set<ParserInputDescriptor> inputDescriptors = this.getInputDescriptors(request);
        Set<Map<String, Object>> inputs = request.inputs();

        this.checkRequiredFields(inputDescriptors, inputs);
        this.checkFieldsType(inputDescriptors, inputs);
        Set<Map<String,Object>> stripedInputs = this.stripeExtras(inputDescriptors, inputs);
        return new CapabilityRequest(request.name(), stripedInputs);
    }

    private Set<Map<String, Object>> stripeExtras(Set<ParserInputDescriptor> inputDescriptors, Set<Map<String, Object>> inputs) {
        return inputs
                .stream()
                .filter(input -> {
                    String inputName = getInputName(input);
                    return inputDescriptors
                            .stream()
                            .anyMatch(desc -> Objects.equals(desc.name(), inputName));
                })
                .collect(Collectors.toSet());
    }

    private void checkFieldsType(
            Set<ParserInputDescriptor> inputDescriptors,
            Set<Map<String, Object>> inputs
    ) {
        for (Map<String, Object> input : inputs) {
            String inputName = getInputName(input);
            String inputType = getInputType(input, inputName);

            ParserInputDescriptor inputDescriptor = inputDescriptors.stream()
                    .filter(desc -> Objects.equals(desc.name(), inputName))
                    .findFirst()
                    .orElse(
                            new ParserInputDescriptor(inputType, inputName, false)
                    );

            boolean inputMatchesDescriptor =
                    Objects.equals(inputDescriptor.type(), inputType);

            if (!inputMatchesDescriptor)
                throw new WrongInputTypeError(
                        inputDescriptor.name(),
                        inputDescriptor.type(),
                        inputType
                );
        }
    }

    private String getInputName(Map<String, Object> input) {
        return (String) input.keySet().toArray()[0];
    }

    private String getInputType(Map<String, Object> input, String inputName) {
        Object inputValue = input.get(inputName);
        return ((Object) (inputValue)).getClass().getSimpleName();
    }

    private Set<ParserInputDescriptor> getInputDescriptors(CapabilityRequest request) {
        Agent agent = capabilityAgentFinder
                .findAgentByCapabilityName(request.name());
        return agent
                .getCapability()
                .parserInputDescriptors();
    }

    private void checkRequiredFields(
            Set<ParserInputDescriptor> inputDescriptors,
            Set<Map<String, Object>> inputs
    ) {
        for (ParserInputDescriptor descriptor : inputDescriptors) {
            boolean containKey = inputs
                    .stream().anyMatch(
                            map -> map.containsKey(descriptor.name())
                    );

            boolean isMissingRequired = descriptor.required() && !containKey;

            if (isMissingRequired)
                throw new MissingRequiredInputError(
                        descriptor.name(),
                        descriptor.type()
                );
        }
    }
}
