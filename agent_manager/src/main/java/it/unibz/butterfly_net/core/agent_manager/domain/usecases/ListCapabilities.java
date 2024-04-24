package it.unibz.butterfly_net.core.agent_manager.domain.usecases;

import it.unibz.butterfly_net.core.agent_manager.domain.model.CapabilityFinder;
import it.unibz.butterfly_net.core.agent_manager.domain.model.CapabilityOffer;

import java.util.List;

public class ListCapabilities {
    private final CapabilityFinder capabilityFinder;

    public ListCapabilities(CapabilityFinder capabilityFinder) {
        this.capabilityFinder = capabilityFinder;
    }

    public List<CapabilityOffer> listAllCapabilities() {
        return this.capabilityFinder.getAll();
    }
}
