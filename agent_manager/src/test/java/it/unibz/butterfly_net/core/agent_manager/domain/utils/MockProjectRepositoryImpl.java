package it.unibz.butterfly_net.core.agent_manager.domain.utils;

import it.unibz.butterfly_net.core.agent_manager.domain.model.Project;
import it.unibz.butterfly_net.core.agent_manager.domain.model.ProjectRepository;

public class MockProjectRepositoryImpl implements MockProjectRepository {
    private ProjectRepository behavior = (Long id) -> {
        throw new NoSuchMethodError("mock");
    };

    @Override
    public Project findById(Long id) {
        return this.behavior.findById(id);
    }

    @Override
    public void mockFind(ProjectRepository mockedBehavior) {
        this.behavior = mockedBehavior;
    }
}
