package it.unibz.butterfly_net.core.agent_manager.domain.utils;

import it.unibz.butterfly_net.core.agent_manager.domain.model.ProjectRepository;

public interface MockProjectRepository extends ProjectRepository {
    void mockFind(ProjectRepository mockedBehavior);
}
